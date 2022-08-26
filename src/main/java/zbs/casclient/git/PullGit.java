package zbs.casclient.git;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.errors.NoRemoteRepositoryException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.TagOpt;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zbs
 * @since 2022/8/25 14:45
 */
public class PullGit {
    private static final String LOCAL_BRANCH_REF_PREFIX = "refs/remotes/origin/";

    String uri = "http://git.xy51.com/java-server/springcloudconfig.git";
    String username = "developer";
    String pwd = "developer_1022";
    String branch = "release";
    String baseDir = "D:\\data\\config-repo";

    /**
     * Flag to indicate that the repository should force pull. If true discard any local
     * changes and take from remote repository.
     */
    private final boolean forcePull = false;

    public static void main(String[] args) throws IOException {
        PullGit pullGit = new PullGit();
        //clone或拉取指定分支代码到baseDir
        String version = pullGit.refresh("release");
        System.out.println(version);

//        Git git = pullGit.openGitRepository();
        
    }

    //创建gitClient，如果baseDir目录存在git仓库，则返回本地仓库；否则，git clone到baseDir目录
    public Git createGitClient() throws IOException, GitAPIException {
        File lock = new File(baseDir, ".git/index.lock");
        if (lock.exists()) {
            // The only way this can happen is if another JVM (e.g. one that
            // crashed earlier) created the lock. We can attempt to recover by
            // wiping the slate clean.
            lock.delete();
        }
        if (new File(baseDir, ".git").exists()) {
            //git文件存在
            return openGitRepository();
        } else {
            //git clone
            return cloneToBasedir();
        }
    }

    //打开本地git仓库
    public Git openGitRepository() throws IOException {
        return Git.open(new File(baseDir));
    }

    //git clone 到指定目录
    public synchronized Git cloneToBasedir() throws GitAPIException {
        if (uri.startsWith("file:")) {
            throw new RuntimeException("暂不支持file");
        }
        File file = new File(baseDir);
        if (!delFile(file) || !file.mkdir()) {
            throw new RuntimeException("文件创建/删除失败: " + baseDir);
        }
        CloneCommand clone = Git.cloneRepository()
                .setURI(uri)  //远程仓库位置
                .setDirectory(file) //本地仓库位置
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, pwd)) //身份验证
                .setCloneSubmodules(false) //是否克隆子仓库
                .setBranch(branch) //克隆分支
                .setTimeout(5);
        try {
            return clone.call();
        } catch (GitAPIException e) {
            delFile(new File(baseDir));
            throw e;
        }
    }

    //删除文件夹及其子文件
    public static boolean delFile(File file) {
        if (!file.exists()) {
            return true;
        }
        if (!file.isDirectory()) {
            return file.delete();
        }
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return file.delete();
        }
        boolean flag = true;
        for (File f : files) {
            flag = flag && delFile(f);
        }
        return flag && file.delete();
    }

    public boolean shouldPull(Git git) throws GitAPIException {
        boolean shouldPull;

        Status gitStatus;
        try {
            gitStatus = git.status().call();
        } catch (JGitInternalException e) {
            onPullInvalidIndex(git, e);
            gitStatus = git.status().call();
        }



        boolean isWorkingTreeClean = gitStatus.isClean();
        String originUrl = git.getRepository().getConfig().getString("remote", "origin", "url");

        if (this.forcePull && !isWorkingTreeClean) {
            shouldPull = true;
            logDirty(gitStatus);
        } else {
            shouldPull = isWorkingTreeClean && originUrl != null;
        }
        if (!isWorkingTreeClean && !this.forcePull) {
            System.out.println("Cannot pull from remote " + originUrl + ", the working tree is not clean.");
        }
        return shouldPull;
    }

    private void onPullInvalidIndex(Git git, JGitInternalException e) {
        if (!e.getMessage().contains("Short read of block.")) {
            throw e;
        }
        if (!this.forcePull) {
            throw e;
        }
        try {
            new File(baseDir, ".git/index").delete();
            git.reset().setMode(ResetCommand.ResetType.HARD).setRef("HEAD").call();
        } catch (GitAPIException ex) {
            e.addSuppressed(ex);
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    private void logDirty(Status status) {
        Set<String> dirties = dirties(status.getAdded(), status.getChanged(), status.getRemoved(), status.getMissing(),
                status.getModified(), status.getConflicting(), status.getUntracked());
        System.out.printf("Dirty files found: %s%n", dirties);
    }

    @SuppressWarnings("unchecked")
    private Set<String> dirties(Set<String>... changes) {
        Set<String> dirties = new HashSet<>();
        for (Set<String> files : changes) {
            dirties.addAll(files);
        }
        return dirties;
    }

    /**
     * clone/拉取 指定分支代码到baseDir，返回version
     *
     * @param label 分支/标签
     */
    public String refresh(String label) {
        Git git = null;
        try {
            git = createGitClient();
            if (shouldPull(git)) {
                fetch(git, label);
            }

            // checkout after fetch so we can get any new branches, tags, ect.
            // if nothing to update so just checkout and merge.
            // Merge because remote branch could have been updated before
            checkout(git, label);
            tryMerge(git, label);

            // always return what is currently HEAD as the version
            return git.getRepository().findRef("HEAD").getObjectId().getName();
        } catch (RefNotFoundException e) {
            throw new RuntimeException("No such label: " + label, e);
        } catch (NoRemoteRepositoryException e) {
            throw new RuntimeException("No such repository: " + uri, e);
        } catch (GitAPIException e) {
            throw new RuntimeException("Cannot clone or checkout repository: " + uri, e);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot load environment", e);
        } finally {
            try {
                if (git != null) {
                    git.close();
                }
            } catch (Exception e) {
                System.out.println("Could not close git repository");
                e.printStackTrace();
            }
        }
    }

    //拉取代码
    public void fetch(Git git, String label) {
        FetchCommand fetch = git.fetch()
                .setRemote("origin")
                .setTagOpt(TagOpt.FETCH_TAGS)
                .setRemoveDeletedRefs(false)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, pwd))
                .setTimeout(3000);

        try {
            FetchResult result = fetch.call();
            if (result.getTrackingRefUpdates() != null && result.getTrackingRefUpdates().size() > 0) {
                System.out.println("Fetched for remote " + label + " and found " + result.getTrackingRefUpdates().size() + " updates");
            }
        } catch (Exception ex) {
            String message = "Could not fetch remote for " + label + " remote: "
                    + git.getRepository().getConfig().getString("remote", "origin", "url");
            System.out.println("warn !!! " + message);
            ex.printStackTrace();
        }
    }

    private void checkout(Git git, String label) throws GitAPIException {
        CheckoutCommand checkout = git.checkout();
        if (GitUtil.shouldTrack(git, label)) {
            GitUtil.trackBranch(git, checkout, label);
        } else {
            // works for tags and local branches
            checkout.setName(label);
        }
        checkout.call();
    }

    private void tryMerge(Git git, String label) {
        try {
            if (GitUtil.isBranch(git, label)) {
                // merge results from fetch
                merge(git, label);
                if (!GitUtil.isClean(git, label)) {
                    System.out.println("warn!!!");
                    System.out.println("The local repository is dirty or ahead of origin. Resetting" + " it to origin/" + label + ".");
                    resetHard(git, label, LOCAL_BRANCH_REF_PREFIX + label);
                }
            }
        } catch (GitAPIException e) {
            throw new RuntimeException("Cannot clone or checkout repository: " + uri, e);
        }
    }

    private void resetHard(Git git, String label, String ref) {
        ResetCommand reset = git.reset();
        reset.setRef(ref);
        reset.setMode(ResetCommand.ResetType.HARD);
        try {
            Ref resetRef = reset.call();
            if (resetRef != null) {
                System.out.println("Reset label " + label + " to version " + resetRef.getObjectId());
            }
        } catch (Exception ex) {
            String message = "Could not reset to remote for " + label + " (current ref=" + ref + "), remote: "
                    + git.getRepository().getConfig().getString("remote", "origin", "url");
            System.out.println(message);
            ex.printStackTrace();
        }
    }

    private void merge(Git git, String label) {
        try {
            MergeCommand merge = git.merge();
            merge.include(git.getRepository().findRef("origin/" + label));
            MergeResult result = merge.call();
            if (!result.getMergeStatus().isSuccessful()) {
                System.out.println("warn!!!");
                System.out.println("Merged from remote " + label + " with result " + result.getMergeStatus());
            }
        } catch (Exception ex) {
            String message = "Could not merge remote for " + label + " remote: "
                    + git.getRepository().getConfig().getString("remote", "origin", "url");
            //warn(message, ex);
            System.out.println(message);
            ex.printStackTrace();
        }
    }

}
