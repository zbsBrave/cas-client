package zbs.casclient.git;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.BranchTrackingStatus;
import org.eclipse.jgit.lib.Ref;

import java.util.List;

/**
 * @author zbs
 * @since 2022/8/25 18:14
 */
public class GitUtil {
    public static void trackBranch(Git git, CheckoutCommand checkout, String label) {
        checkout.setCreateBranch(true).setName(label).setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                .setStartPoint("origin/" + label);
    }
    
    public static boolean shouldTrack(Git git, String label) throws GitAPIException {
        return isBranch(git, label) && !isLocalBranch(git, label);
    }
    
    
    public static boolean isBranch(Git git, String label) throws GitAPIException {
        return containsBranch(git, label, ListBranchCommand.ListMode.ALL);
    }

    public static boolean isLocalBranch(Git git, String label) throws GitAPIException {
        return containsBranch(git, label, null);
    }

    public static boolean containsBranch(Git git, String label, ListBranchCommand.ListMode listMode) throws GitAPIException {
        ListBranchCommand command = git.branchList();
        if (listMode != null) {
            command.setListMode(listMode);
        }
        List<Ref> branches = command.call();
        for (Ref ref : branches) {
            if (ref.getName().endsWith("/" + label)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isClean(Git git, String label) {
        StatusCommand status = git.status();
        try {
            BranchTrackingStatus trackingStatus = BranchTrackingStatus.of(git.getRepository(), label);
            boolean isBranchAhead = trackingStatus != null && trackingStatus.getAheadCount() > 0;
            return status.call().isClean() && !isBranchAhead;
        }
        catch (Exception e) {
            String message = "Could not execute status command on local repository. Cause: ("
                    + e.getClass().getSimpleName() + ") " + e.getMessage();
            System.out.println(message);
            e.printStackTrace();
            //warn(message, e);
            return false;
        }
    }
}
