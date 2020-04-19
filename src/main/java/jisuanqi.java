import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
public class jisuanqi{
    public static class ButtonHandler implements ActionListener{
        public JTextField tf;
        private Stack<Character> lstack = null;
        public static boolean symbol =false;
        public static boolean comm =true;
        public static boolean bracket =false;
        ButtonHandler(JTextField tf) {
            this.tf = tf;
        }
        private boolean isNumber(char num) {
            return num >= '0' && num <= '9' || num == '.';
        }
        private boolean comparePri(char s) {
            if (lstack.empty()) return true;
            char top = lstack.peek();
            if (top == '(') return true;
            switch (s) {
                case '(': return true;
                case 'x':
                case '/':
                    return top == '+' || top == '-';
                case '+':
                case '-':
                case '=':
                case ')':
                    return false;
                default:break;
            }
            return true;
        }
        public double calculate(String numStr) {
            if (numStr.length() > 1 && !"=".equals(numStr.charAt(numStr.length() - 1) + ""))
                numStr += "=";
            Stack<Double> numberStack = new Stack<>();
            lstack = new Stack<>();
            StringBuffer temp = new StringBuffer();
            for (int i = 0; i < numStr.length(); i++) {
                char ch = numStr.charAt(i);
                if (isNumber(ch)) temp.append(ch);
                else {
                    String tempStr = temp.toString();
                    if (!tempStr.isEmpty()) {
                        double num = Double.parseDouble(tempStr);
                        numberStack.push(num);
                        temp = new StringBuffer();
                    }
                    while (!comparePri(ch) && !lstack.empty()) {
                        double b = numberStack.pop();
                        double a = numberStack.pop();
                        switch (lstack.pop()) {
                            case '+':
                                numberStack.push(a + b);break;
                            case '-':
                                numberStack.push(a - b);break;
                            case 'x':
                                numberStack.push(a * b);break;
                            case '/':
                                numberStack.push(a / b);break;
                            default:break;
                        }
                    }
                    if (ch != '=') {
                        lstack.push(ch);
                        if (ch == ')') {
                            lstack.pop();
                            lstack.pop();
                        }
                    }
                }
            }
            return numberStack.pop();
        }
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().charAt(0)>='0'&&e.getActionCommand().charAt(0)<='9')
            {
                tf.setText(tf.getText()+e.getActionCommand());
                symbol =true;
            }
            else if(e.getActionCommand().equals(")") && bracket) {
                tf.setText(tf.getText()+")");
                bracket =false;
            }
            else if(e.getActionCommand().equals("+") && symbol) {
                tf.setText(tf.getText()+"+");
                symbol =false;
                comm =true;
            }
            else if(e.getActionCommand().equals("-") && symbol) {
                tf.setText(tf.getText()+"-");
                symbol =false;
                comm =true;
            }
            else if(e.getActionCommand().equals("x") && symbol) {
                tf.setText(tf.getText()+"x");
                symbol =false;
                comm =true;
            }
            else if(e.getActionCommand().equals("/") && symbol) {
                tf.setText(tf.getText()+"/");
                symbol =false;
                comm =true;
            }
            else if(e.getActionCommand().equals("(") &&!bracket) {
                tf.setText(tf.getText()+"(");
                bracket =true;
            }
            else if(e.getActionCommand().equals(".") && comm) {
                tf.setText(tf.getText()+".");
                comm =false;
            }
            else if(e.getActionCommand().equals("C")) tf.setText("");
            else if(e.getActionCommand().equals("D")) {
                String str=tf.getText();
                str=str.substring(0, str.length()-1);
                tf.setText(str);
            }
            else if(e.getActionCommand().equals("=")) {
                String str=tf.getText();
                str=str+"=";
                double result=calculate(str);
                if(result==(int)result||Math.abs(result-(int)result)<1e-8) tf.setText((int)result+"");
                else tf.setText(result+"");
            }
        }
    }
    public void win() {
        JFrame f =new JFrame();
        f.setTitle("橙色计算器");
        f.setSize(500,649);
        f.setLocationRelativeTo(null);
        f.setResizable(false);
        f.setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextField tf=new JTextField(50);
        tf.setBounds(0,0,500,210);
        tf.setFont(new Font("宋体",Font.PLAIN,50));
        tf.setEditable(false);
        tf.setBackground(Color.orange);
        JButton [] b =new JButton[22];
        b[1]=new JButton("7");
        b[1].setBounds(0,210,100,100);
        b[2]=new JButton("8");
        b[2].setBounds(100,210,100,100);
        b[3]=new JButton("9");
        b[3].setBounds(200,210,100,100);
        b[4]=new JButton("D");
        b[4].setBounds(300,210,100,100);
        b[5]=new JButton("4");
        b[5].setBounds(0,310,100,100);
        b[6]=new JButton("5");
        b[6].setBounds(100,310,100,100);
        b[7]=new JButton("6");
        b[7].setBounds(200,310,100,100);
        b[8]=new JButton("-");
        b[8].setBounds(300,310,100,100);
        b[9]=new JButton("1");
        b[9].setBounds(0,410,100,100);
        b[10]=new JButton("2");
        b[10].setBounds(100,410,100,100);
        b[11]=new JButton("3");
        b[11].setBounds(200,410,100,100);
        b[12]=new JButton("+");
        b[12].setBounds(300,410,100,100);
        b[13]=new JButton("0");
        b[13].setBounds(0,510,100,100);
        b[14]=new JButton("(");
        b[14].setBounds(100,510,100,100);
        b[15]=new JButton(")");
        b[15].setBounds(200,510,100,100);
        b[16]=new JButton(".");
        b[16].setBounds(300,510,100,100);
        b[17]=new JButton("C");
        b[17].setBounds(400,210,100,100);
        b[18]=new JButton("x");
        b[18].setBounds(400,310,100,100);
        b[19]=new JButton("/");
        b[19].setBounds(400,410,100,100);
        b[20]=new JButton("=");
        b[20].setBounds(400,510,100,100);
        f.add(tf);
        for(int i=1;i<=20;i++) {
            b[i].addActionListener(new ButtonHandler(tf));
            b[i].setBackground(Color.orange);
            b[i].setFont(new Font("宋体",Font.PLAIN,60));
            f.add(b[i]);
        }
        f.setVisible(true);
    }
    public static void main(String[] args) {
        jisuanqi w =new jisuanqi();
        w.win();
    }
}