import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class NoMaker extends JFrame {
    private JTextArea prg;
    public NoMaker() {
        super("山东建筑大学管理学院 信息管理 专用工具");
        JPanel textPanel = new JPanel();
        JLabel messageLabel = new JLabel("请将源程序粘贴到下面的文本框中，然后点击下面的按钮");
        textPanel.add(messageLabel);
        prg = new JTextArea(23,45);
        prg.setText("");
        JScrollPane jsPane =new JScrollPane(prg);
        textPanel.add(jsPane);

        this.add(textPanel,BorderLayout.CENTER);



        JPanel controlPanel = new JPanel();
        JButton addNum = new JButton("点击本按钮，程序会自动添加行号并复制到剪切板，请在其它编辑器中粘贴");
        controlPanel.add(addNum);
        this.add(controlPanel,BorderLayout.SOUTH);
        {
            addNum.addActionListener(new ActionListener(){

                public void actionPerformed(ActionEvent arg0) {
                    // TODO Auto-generated method stub
//					System.out.println("prg:"+prg.getText());
                    String content = prg.getText();
//					content.replace("\t", "    ");
                    String[] contentArray = content.split("\n");
                    int len = contentArray.length;
                    //how many lines
                    int digitNum = 0;
                    while(len!=0){
                        len = len / 10;
                        digitNum++;
                    }

                    StringBuilder all = new StringBuilder();
                    String format = "%0"+digitNum+"d";
                    int num = 1;
                    for (String line : contentArray) {
                        all.append(String.format(format,num) + " "+line+"\n");
                        num++;
                    }
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    Transferable trandata = new StringSelection(all.toString());
                    clipboard.setContents(trandata, null);

                }

            });
        }
        this.setSize(600,500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

    }
    public static void main(String[] args){
        new NoMaker();
    }


}
