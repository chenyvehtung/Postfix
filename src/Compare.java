import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 原始Parser，含有尾递归。
 * @author Donald
 */
class ParserWithRec {
	static int lookahead;

	public ParserWithRec() throws IOException {
		lookahead = System.in.read();
	}

	void expr() throws IOException {
		term();
		rest();
	}

	void rest() throws IOException {
		if (lookahead == '+') {
			match('+');
			term();
			System.out.write('+');
			rest();
		} else if (lookahead == '-') {
			match('-');
			term();
			System.out.write('-');
			rest();
		} else {
			// do nothing with the input
		}
	}

	void term() throws IOException {
		if (Character.isDigit((char)lookahead)) {
			System.out.write((char)lookahead);
			match(lookahead);
		} else  throw new Error("syntax error");
	}

	void match(int t) throws IOException {
		if (lookahead == t)  lookahead = System.in.read();
		else  throw new Error("syntax error");
	}
}

/**
 * 消除尾递归后的Parser。使用while循环来消除尾递归。
 * @author Donald
 */
class ParserNoRec {
	static int lookahead;

	public ParserNoRec() throws IOException {
		lookahead = System.in.read();
	}

	void expr() throws IOException {
		term();
		rest();
	}

	void rest() throws IOException {
		int tempAhead;
		while(lookahead == '+' || lookahead == '-') {
			tempAhead = lookahead;
			match(tempAhead);
			term();
			System.out.write(tempAhead);
		}
	}

	void term() throws IOException {
		if (Character.isDigit((char)lookahead)) {
			System.out.write((char)lookahead);
			match(lookahead);
		} else  throw new Error("syntax error");
	}

	void match(int t) throws IOException {
		if (lookahead == t)  lookahead = System.in.read();
		else  throw new Error("syntax error");
	}
}

/**
 * 生成中缀表达式。
 * @author Donald
 */
class CreateInfix {
	/**
	 * 随机生成包含有num个操作数的中缀表达式
	 * @param num 操作数个数
	 * @return 包含有num个操作数的合法的中缀表达式
	 */
	String getInfix(int num) {
		String infix = "";
		String oper = "+-";
		infix += (int)(Math.random()*10);
		for (int i = 0; i < num - 1; i++) {
			infix += oper.charAt((int)(Math.random()*2));
			infix += (int)(Math.random()*10);
		}
		return infix;
	}
}

/**
 * 测试消除尾递归前后的中缀表达式的性能。<br>
 * 总共进行15次测试，从1000个操作数开始，每次对有无尾递归的Parser分别跑100次并求出平均时间。每个循环增加1000个操作数。
 * 得到的结果保存在result.txt文件中。
 * @author Donald
 * @see ParserWithRec
 * @see ParseNoRec
 * @see CreateInfix
 */
public class Compare {
	public static void main(String[] args) throws IOException {
		String testInfix = "";
		int num = 0;
		long startTime, endTime;
		final int cnt = 100;
		PrintWriter writer = new PrintWriter("result.txt", "UTF-8");
		//PrintWriter writer2 = new PrintWriter("infix.txt", "UTF-8");
		writer.println("OperNum\tTime(ms)\tRecursion");
		for (int i = 1; i <= 15; i++) {
			num = 1000 * i;
			String strNum = Integer.toString(num);
			testInfix = new CreateInfix().getInfix(num);
			//writer2.println(testInfix);
			//writer2.flush();
			Boolean catchExcep = false;
		
			//test of parser without recursion
			startTime = System.currentTimeMillis();
			for (int j = 0; j < cnt; j++) {
				System.setIn(new ByteArrayInputStream( testInfix.getBytes() ));
				try {
					new ParserNoRec().expr();
				} catch (StackOverflowError e) {
					writer.println(e.toString() + ", from parser without recursion, strNum=" + strNum);
					writer.flush();
					catchExcep = true;
					break;
				}
				System.setIn(null);
			}
			endTime = System.currentTimeMillis();
			if (!catchExcep) {
				writer.println(strNum + "\t" + (endTime-startTime)/(float)cnt + "\tNo");
				writer.flush();
			}
						
			//test of parser with recursion
			startTime = System.currentTimeMillis();
			for (int j = 0; j < cnt; j++) {	
				System.setIn(new ByteArrayInputStream( testInfix.getBytes() ));
				try {
					new ParserWithRec().expr();
				} catch (StackOverflowError e) {
					writer.println(e.toString() + ", from parser with recursion, strNum=" + strNum);
					writer.flush();
					catchExcep = true;
					break;
				}
				System.setIn(null);
			}
			endTime = System.currentTimeMillis();
			if (!catchExcep) {
				writer.println(strNum + "\t" + (endTime-startTime)/(float)cnt + "\tYes");
				writer.flush();		
			}
		}
		writer.close();
		//writer2.close();
	}
}


