package cmd;

import net.ICallBack;

public class CmdExecutor {
    private String cmd = null;
    private String[] params = null;

    private ICallBack<String[]> initCallBack = null;
    private ICallBack<String[]> moveToCallBack = null;
    private ICallBack<String[]> loseCallBack = null;

    public CmdExecutor(ICallBack<String[]> initCallBack,ICallBack<String[]> moveToCallBack,ICallBack<String[]> loseCallBack){
        this.initCallBack = initCallBack;
        this.moveToCallBack = moveToCallBack;
        this.loseCallBack = loseCallBack;
    }

    public void execute(String cmdMsg) {
        handleCmdMsg(cmdMsg);
        switch (cmd) {
            case "init":
                initCallBack.invoke(params);
                break;
            case "moveto":
                moveToCallBack.invoke(params);
                break;
            case "lose":
                //远程玩家输了,其实不应该传参
                loseCallBack.invoke(params);
                break;
        }
    }

    private void handleCmdMsg(String cmdMsg) {
        var arr = cmdMsg.split("\\|");
        if (arr.length == 2) {
            cmd = arr[0];
            params = arr[1].split("#");
        } else {cmd = arr[0];params = null;}
    }
}
