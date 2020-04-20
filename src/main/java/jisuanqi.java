import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.*;
public class jisuanqi{
    public static class ButtonHandler implements ActionListener{
        public JTextField tf;
        public static boolean sy =false;
        public static boolean c =true;
        public static boolean fu=true;
        public static int k =0;
        private static final Map<Character, Integer> basic = new HashMap<>();
        static {
            basic.put('-', 2);
            basic.put('+', 2);
            basic.put('x', 3);
            basic.put('/', 3);//
            basic.put('(', 1);//在运算中  （）的优先级最高，但是此处因程序中需要 故设置为0
            basic.put('#',0);
        }
        ButtonHandler(JTextField tf) {
            this.tf = tf;
        }
        List<String>ans=new ArrayList<>();
        public void trans(String str)
        {
            String standard = "x/+-()";
            Stack<Character> ops = new Stack<>();
            ops.push('#');
            StringBuilder tempStr;
            for (int i = 0; i < str.length(); i++)
            {   // 检查是否是带符号的数字
                // 1. 带正负号且前一个字符为运算符（i=0时直接带正负号的也是数字）
                // 2. 当前字符为数字
                if( ((str.charAt(i) == '-' || str.charAt(i) == '+') && (i == 0 || "x/+-(".indexOf(str.charAt(i-1))!=-1)) || Character.isDigit(str.charAt(i)) )
                {   // 把操作数加入到后缀式中
                    // 如果是正号就不用加入，负号或者数字本身都要加入
                    tempStr = new StringBuilder(str.charAt(i) != '+' ? str.substring(i, i + 1) : "");
                    while (i + 1 < str.length() && standard.indexOf(str.charAt(i+1))==-1)
                    {
                        i++;
                        tempStr.append(str.substring(i, i + 1));
                    }
                    ans.add(tempStr.toString());
                }else { // 出现操作符
                    if(str.charAt(i) == '(')
                        ops.push(str.charAt(i));
                    else if(str.charAt(i) == ')') {
                        while (ops.peek() != '(')
                        {
                            ans.add(ops.peek().toString());
                            ops.pop();
                        }
                        ops.pop();
                    }else {
                        while (basic.get(str.charAt(i))<=basic.get(ops.peek()))
                        {
                            ans.add(ops.peek().toString());
                            ops.pop();
                        }
                        ops.push(str.charAt(i));
                    }
                }
            }
            while (ops.size() > 1)
            {
                ans.add(ops.peek().toString());
                ops.pop();
            }
        }
        public static BigDecimal Suff(List<String> suffixExp) {
            Stack<BigDecimal> numStack = new Stack<>();
            for (String str : suffixExp) {
                if (str.length() == 1 && isOperation(str.charAt(0))) {// 如果是操作符
                    BigDecimal num2 = numStack.pop();
                    BigDecimal num1 = numStack.pop();
                    numStack.push(calc(num1, num2, str));

                } else {
                    numStack.push(new BigDecimal(str));
                }
            }
            return numStack.peek();
        }

        public static BigDecimal calc(BigDecimal num1, BigDecimal num2, String op) {
            switch (op) {
                case "+":
                    return num1.add(num2);
                case "-":
                    return num1.subtract(num2);
                case "x":
                    return num1.multiply(num2);
                case "/":
                    return num1.divide(num2, 15, RoundingMode.HALF_UP);// 除法保留15位小数，四舍五入
                default:
                    return num1;
            }
        }

        public static boolean isOperation(char ch) {
            return ch == '+' || ch == '-' || ch == 'x' || ch == '/';
        }

        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().charAt(0)>='0'&&e.getActionCommand().charAt(0)<='9')
            {
                tf.setText(tf.getText()+e.getActionCommand());
                fu=sy =c=true;
            }
            else if(e.getActionCommand().equals(")") && k-->0)
                tf.setText(tf.getText()+")");
            else if(e.getActionCommand().equals("+") && sy) {
                tf.setText(tf.getText()+"+");
                sy = c =false;
            }
            else if(e.getActionCommand().equals("-") && fu) {
                tf.setText(tf.getText()+"-");
                sy=fu = c =false;
            }
            else if(e.getActionCommand().equals("x") && sy) {
                tf.setText(tf.getText()+"x");
                sy = c =false;
            }
            else if(e.getActionCommand().equals("/") && sy) {
                tf.setText(tf.getText()+"/");
                sy = c =false;
            }
            else if(e.getActionCommand().equals("(") ) {
                tf.setText(tf.getText()+"(");
                k++;
                fu=true;
            }
            else if(e.getActionCommand().equals(".") && c) {
                tf.setText(tf.getText()+".");
                c =false;
            }
            else if(e.getActionCommand().equals("C")) {
                tf.setText("");
                k=0;c=fu=true;sy=false;
            }
            else if(e.getActionCommand().equals("D")) {
                String str=tf.getText(); int m=str.length()-1;
                char o=str.charAt(m);
                if(o=='(') k--;
                else if(o==')') k++;
                else if(o=='x'||o=='/'||o=='-'||o=='+')
                    fu=sy = c =true;
                str=str.substring(0,m);
                tf.setText(str);
            }
            else if(e.getActionCommand().equals("=")) {
                String s=tf.getText();
                if(s.charAt(0)=='-')s='0'+s;
                ans.clear();
                // 计算后缀表达式
                trans(s);
                for(String ss:ans)
                    System.out.println(ss);
                BigDecimal res = Suff(ans);
                String ak=res.toString();
                int d;
                for(int i = ak.length()-1;true; i--)
                    if(ak.charAt(i)!='0')
                    { d=i;break;}
                if(ak.charAt(d)=='.')d--;
                tf.setText(ak.substring(0,d+1)+"");
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