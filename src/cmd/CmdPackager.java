package cmd;

public class CmdPackager {
    private static CmdPackager cmdPackager = null;

    public static CmdPackager getInstance() {
        if (cmdPackager == null) {
            cmdPackager = new CmdPackager();
        }
        return cmdPackager;
    }

    public String pack(String cmd, String[] params) {
        if (params == null) {
            return cmd + "|";
        } else {
            var p = String.join("#", params);
            return cmd + "|" + p;
        }
    }
}
