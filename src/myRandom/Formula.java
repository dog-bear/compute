package myRandom;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Creat by 黄伟洪 on 2020.4.4.
 */
public class Formula {

    java.util.Random random = new java.util.Random();
    int num = random.nextInt(3)+1;//随机生成1-3
    Object[] objects = new Object[4*num+3];//存储中缀表达式
    TrueFraction[] trueFractions = new TrueFraction[num+1];
    String[] strings = {"+","-","*","/","(",")"};//运算符号

    public void randomTrueFraction(int i,int j){
        TrueFraction trueFraction = new TrueFraction();
        trueFraction.setNumerator(random.nextInt(10));
        while(j>2&&objects[j-2]=="/"&&trueFraction.getNumerator()==0){
            trueFraction.setNumerator(random.nextInt(10));
        }
        trueFraction.setDenominator(random.nextInt(10)+1);
        if(trueFraction.getNumerator()==0){
            objects[j] = 0;
        }else if(trueFraction.getNumerator()%trueFraction.getDenominator()==0){
            objects[j] = trueFraction.getNumerator()/trueFraction.getDenominator();
        }else{
            objects[j] = trueFraction;
        }
        trueFractions[i]=trueFraction;
    }

    public void randomFormula(){

        StringBuffer stringBuffer = new StringBuffer();
        int[] ints = new int[num];

        for(int i=0;i<4*num+3;i++){
            objects[i]=' ';
        }//初始化object数组

        for(int i=0,j=3;i<num;i++,j=j+4){
            objects[j] = strings[random.nextInt(4)];
        }//随机生成运算符

        for(int i=0,j=1;i<num+1;i++,j=j+4){
           randomTrueFraction(i,j);
           /*
           while(i>0&&objects[j-2].equals("-")){
               if(((float)trueFractions[i-1].getNumerator()/trueFractions[i-1].getDenominator())<
                       ((float)trueFractions[i].getNumerator()/trueFractions[i].getDenominator()))
                   randomTrueFraction(i-1,j-4);
               else break;
           }*/
        }//随机生成运算数

        for(int i=0;i<num;i++){
            ints[i]=i*4;
        }//括号生成可能位置

        if(num!=1){
            int temp1=ints[random.nextInt(num)];
            int temp2=ints[random.nextInt(num)]+6;
            while((temp1>=temp2)||(temp1+2==temp2)){
                temp1=ints[random.nextInt(num)];
                temp2=ints[random.nextInt(num)]+6;
            }//生成括号
            objects[temp1]="(";
            objects[temp2]=")";
        }

        System.out.println();
        for(int i=0;i<4*num+3;i++){
            stringBuffer.append(objects[i].toString());
        }
        System.out.print(stringBuffer);
    }

    public void formulaCompute(){

        TrueFraction trueFraction = new TrueFraction();
        StringBuffer stringBuffer = new StringBuffer();

        // 存储操作数的栈
        Stack<Object> opStack = new Stack<Object>();

        // 存储转换后的逆波兰式的队列
        Queue<Object> reversePolish = new LinkedList<Object>();

        for (Object o : objects) {

            // 如果是数字
            if(isDigit(o.toString())||isTrueFraction(o)){

                reversePolish.offer(o);
                // 如果是操作符
            } else if(isOperator(o)){

                //是左括号直接入栈
                if("(".equals(o)){

                    opStack.push(o);
                    // 如果是右括号
                } else if(")".equals(o)){

                    // 把离上一个“（”之间的操作符全部弹出来放入逆波兰式的队列中
                    while(!opStack.isEmpty()){

                        Object op = opStack.peek();
                        if(op.equals("(")){

                            opStack.pop();
                            break;

                        } else{

                            reversePolish.offer(opStack.pop());
                        }
                    }
                } else{

                    while(!opStack.isEmpty()){
                        // 如果栈顶元素为"("直接入栈
                        if("(".equals(opStack.peek())){

                            opStack.push(o);
                            break;
                            //如果栈顶元素优先级大于s
                        }else if(isGreat(opStack.peek(), o)){

                            reversePolish.offer(opStack.pop());

                        }else if(isGreat(o, opStack.peek())){

                            opStack.push(o);
                            break;
                        }

                    }
                    // 如果栈为空，直接入栈
                    if(opStack.isEmpty())

                        opStack.push(o);
                }
            }
        }
        // 将剩余的操作符入队
        while(!opStack.isEmpty()){
                reversePolish.offer(opStack.pop());
        }

        System.out.println();

        while(!reversePolish.isEmpty()){
            stringBuffer.append(reversePolish.peek());
            //System.out.println(reversePolish.peek());
            Object o= reversePolish.poll();
            if(isDigit(o.toString())||isTrueFraction(o)){
                opStack.push(o);
                // 如果是操作符
            }else if(isOperator(o)){
                Object o2 = opStack.pop();
                System.out.println("取出运算数2："+o2.toString());
                Object o1 = opStack.pop();
                System.out.println("取出运算数1："+o1.toString());
                System.out.println("取出运算符："+o.toString());
                switch (o.toString()){
                    case "+":
                        trueFraction=addition(o1,o2);
                        System.out.println("运算结果："+trueFraction);
                        break;
                    case "-":
                        trueFraction=subtraction(o1,o2);
                        System.out.println("运算结果："+trueFraction);
                        break;
                    case "*":
                        trueFraction=multiplication(o1,o2);
                        System.out.println("运算结果："+trueFraction);
                        break;
                    case "/":
                        trueFraction=division(o1,o2);
                        System.out.println("运算结果："+trueFraction);
                        break;
                }
                opStack.push(trueFraction);
            }
        }

        System.out.println();
        System.out.println(stringBuffer);
        System.out.println("运算结果"+opStack.pop().toString());
    }

    static boolean isDigit(String s) {

        for (int i = 0; i < s.length(); i++) {

            if (!Character.isDigit(s.charAt(i)))

                return false;
        }

        return true;
    }

    public boolean isTrueFraction(Object o){
            if(o instanceof TrueFraction){
                return true;
            }
        return false;
    }

    public boolean isOperator(Object o){
        for(String s : strings){
            if(o.equals(s)){
                return true;
            }
        }
        return false;
    }

    static boolean isGreat(Object op1, Object op2) {

        if (getPriority(op1) >=getPriority(op2)) {
            return true;
        } else

            return false;
    }

    static int getPriority(Object op) {

        if ("+".equals(op) || "-".equals(op))

            return 1;

        else if ("*".equals(op) || "/".equals(op))

            return 2;

        else

            throw new IllegalArgumentException("Unsupported operator!");
    }

    public TrueFraction addition(Object o1,Object o2){
        TrueFraction trueFraction = new TrueFraction();
        TrueFraction trueFraction2;
        TrueFraction trueFraction3;
        if(isDigit(o1.toString())&&isDigit(o2.toString())){
            trueFraction.setNumerator((int)o1+(int)o2);
            trueFraction.setDenominator(1);
            return trueFraction;
        }else if(isDigit(o1.toString())&&isTrueFraction(o2)){
            trueFraction2 = (TrueFraction)o2;
            trueFraction.setNumerator(((int)o1)*trueFraction2.getDenominator()+trueFraction2.getNumerator());
            trueFraction.setDenominator(trueFraction2.getDenominator());
        }else if(isDigit(o2.toString())&&isTrueFraction(o1)){
            trueFraction2 = (TrueFraction) o1;
            trueFraction.setNumerator(((int) o2)*trueFraction2.getDenominator()+trueFraction2.getNumerator());
            trueFraction.setDenominator(trueFraction2.getDenominator());
        }else if(isTrueFraction(o1)&&isTrueFraction(o2)){
            trueFraction2 = (TrueFraction) o1;
            trueFraction3= (TrueFraction) o2;
            trueFraction.setNumerator(trueFraction2.getNumerator()*trueFraction3.getDenominator()+trueFraction3.getNumerator()*trueFraction2.getDenominator());
            trueFraction.setDenominator(trueFraction2.getDenominator()*trueFraction3.getDenominator());
        }
        return trueFraction;
    }

    public TrueFraction subtraction(Object o1,Object o2){
        TrueFraction trueFraction = new TrueFraction();
        TrueFraction trueFraction2;
        TrueFraction trueFraction3;
        if(isDigit(o1.toString())&&isDigit(o2.toString())){
            trueFraction.setNumerator((int)o1-(int)o2);
            trueFraction.setDenominator(1);
            return trueFraction;
        }else if(isDigit(o1.toString())&&isTrueFraction(o2)){
            trueFraction2 = (TrueFraction)o2;
            trueFraction.setNumerator(((int)o1)*trueFraction2.getDenominator()-trueFraction2.getNumerator());
            trueFraction.setDenominator(trueFraction2.getDenominator());
        }else if(isDigit(o2.toString())&&isTrueFraction(o1)) {
            trueFraction2 = (TrueFraction) o1;
            trueFraction.setNumerator(trueFraction2.getNumerator()-((int) o2)*trueFraction2.getDenominator());
            trueFraction.setDenominator(trueFraction2.getDenominator());
        }else if(isTrueFraction(o1)&&isTrueFraction(o2)){
            trueFraction2 = (TrueFraction) o1;
            trueFraction3= (TrueFraction) o2;
            trueFraction.setNumerator(trueFraction2.getNumerator()*trueFraction3.getDenominator()-trueFraction3.getNumerator()*trueFraction2.getDenominator());
            trueFraction.setDenominator(trueFraction2.getDenominator()*trueFraction3.getDenominator());
        }
        return trueFraction;
    }

    public TrueFraction multiplication(Object o1,Object o2){
        TrueFraction trueFraction = new TrueFraction();
        TrueFraction trueFraction2;
        TrueFraction trueFraction3;
        if(isDigit(o1.toString())&&isDigit(o2.toString())){
            trueFraction.setNumerator((int)o1*(int)o2);
            trueFraction.setDenominator(1);
            return trueFraction;
        }else if(isDigit(o1.toString())&&isTrueFraction(o2)){
            trueFraction2 = (TrueFraction)o2;
            trueFraction.setNumerator(((int)o1)*trueFraction2.getNumerator());
            trueFraction.setDenominator(trueFraction2.getDenominator());
        }else if(isDigit(o2.toString())&&isTrueFraction(o1)) {
            trueFraction2 = (TrueFraction) o1;
            trueFraction.setNumerator(trueFraction2.getNumerator()*((int) o2));
            trueFraction.setDenominator(trueFraction2.getDenominator());
        }else if(isTrueFraction(o1)&&isTrueFraction(o2)){
            trueFraction2 = (TrueFraction) o1;
            trueFraction3= (TrueFraction) o2;
            trueFraction.setNumerator(trueFraction2.getNumerator()*trueFraction3.getNumerator());
            trueFraction.setDenominator(trueFraction2.getDenominator()*trueFraction3.getDenominator());
        }
        return trueFraction;
    }

    public TrueFraction division(Object o1,Object o2){
        TrueFraction trueFraction = new TrueFraction();
        TrueFraction trueFraction2;
        TrueFraction trueFraction3;
        if(isDigit(o1.toString())&&isDigit(o2.toString())){
            trueFraction.setNumerator((int)o1);
            trueFraction.setDenominator((int)o2);
            return trueFraction;
        }else if(isDigit(o1.toString())&&isTrueFraction(o2)){
            trueFraction2 = (TrueFraction)o2;
            trueFraction.setNumerator(((int)o1)*trueFraction2.getDenominator());
            trueFraction.setDenominator(trueFraction2.getNumerator());
        }else if(isDigit(o2.toString())&&isTrueFraction(o1)) {
            trueFraction2 = (TrueFraction) o1;
            trueFraction.setNumerator(trueFraction2.getDenominator()*((int) o2));
            trueFraction.setDenominator(trueFraction2.getNumerator());
        }else if(isTrueFraction(o1)&&isTrueFraction(o2)){
            trueFraction2 = (TrueFraction) o1;
            trueFraction3= (TrueFraction) o2;
            trueFraction.setNumerator(trueFraction2.getNumerator()*trueFraction3.getDenominator());
            trueFraction.setDenominator(trueFraction2.getDenominator()*trueFraction3.getNumerator());
        }
        return trueFraction;

    }
}
