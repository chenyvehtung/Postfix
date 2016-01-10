import java.io.*;
import java.util.ArrayList;

/**
 * PanicParser是一个简单的词法分析器，它的基本框架由DBv2第2章提供。它接受的输入是一个中缀表达式，且该中缀表达式必须遵循以下规则：
 * <ul>
 * <li>表达式中的运算量只能是0~9的单个数字
 * <li>表达式中的运算符只能包含加('+')、减('-')
 * <li>表达式中不能有任何空格，制表符等空白符号
 * </ul>
 * 它的最终输出是一个后缀表达式，如果表达式输入有错误，它会记录错误类型，包含：
 * <ul>
 * <li>Syntax Error 语法错误
 * <li>Lexical Error 词法错误
 * </ul>
 * 另外，本程序采用恐慌模式（panic mode）对error进行恢复，最终给出一个正确的后缀表达式以及相应的出错位置和信息
 * @author Donald
 *
 */
class PanicParser {
	/**
	 * 静态成员变量，用来存取读到的下一位字符
	 */
	static int lookahead;
	/**
	 * 私有成员变量，用来记录当前字符的位置
	 */
	private int cnt;
	/**
	 * 私有成员变量，用来记录表达式是否已经匹配到第一个数字。
	 */
	private Boolean started = false;
	/**
	 * 静态成员数组，用来记录出错信息。
	 */
	static ArrayList<Error> errors = new ArrayList<Error>();
	/**
	 * PanicParser类的构造函数。
	 * 它会读取一个字符并且存储到lookahead变量中。
	 * 
	 * @throws IOException IO流异常
	 * @see lookahead
	 */
	public PanicParser() throws IOException {
		lookahead = System.in.read();
		cnt = 1;
	}
	/**
	 * expr()读取中缀表达式并将对应的后缀表达式打印出来。通过调用函数term()和rest()来实现。
	 * 
	 * @throws IOException IO流异常
	 * @see #term()
	 * @see #rest()
	 */
	void expr() throws IOException {
		term();
		rest();
	}
	/**
	 * rest()是PanicParser类中最核心的函数。
	 * 它匹配‘+’，‘-’符号，然后读取下一个字符并打印出该运算量，再将运算符打印出，这样便实现了中缀转后缀。
	 * 它会循环执行，直到输入出现错误或者输入结束。<br>
	 * 出现错误时，记录词法错误信息，并采用恐慌模式一直向后读取，直到读取到一个正确的符号为止。
	 * 
	 * @throws IOException IO流异常
	 * @see #match(int)
	 * @see #term()
	 */
	void rest() throws IOException {
		int tempAhead;
		while(lookahead == '+' || lookahead == '-') {
			tempAhead = lookahead;
			match(tempAhead);
			term();
			System.out.write(tempAhead);
		}
		//13是回车，当从终端手动输入时的最后一个字符；-1是测试时用到的字符串输入的最后一个字符
		if ( !(lookahead == 13 || lookahead == -1)) {
			if (Character.isDigit((char)lookahead))
				errors.add(new Error("Lexical Error" + errPosition() + "Expression only supports Unit."));
			else
				errors.add(new Error("Lexical Error" + errPosition() + "The operator can only be + or -."));
			match(lookahead);
			rest();
		}
	}
	/**
	 * term()匹配运算量。
	 * 如果是运算量，就会继续读取下一个字符。否则记录语法错误信息，并一直向后读取，直到读取到一个数字为止。
	 * 
	 * @throws IOException IO流异常
	 * @see #match(int)
	 * @see started
	 */
	void term() throws IOException {
		if (Character.isDigit((char)lookahead)) {
			System.out.write((char)lookahead);
			match(lookahead);
			started = true;
		} 
		else {
			if (!started) 
				errors.add(new Error("Syntax Error" + errPosition() + "Expression should be started with a digit."));
			else 
				errors.add(new Error("Syntax Error" + errPosition() + "Right operand is needed."));
			match(lookahead);
			term();
		}
	}
	/**
	 * match()匹配lookahead。
	 * 如果lookahead相同，就读取下一个字符用来更新lookahead，同时将cnt自增一，否则抛出语法错误信息。
	 * 
	 * @param t 字符，用来与lookahead进行比对
	 * @throws IOException IO流异常
	 * @throws Error 语法错误(Syntax Error)异常
	 * @see lookahead
	 * @see cnt
	 */
	void match(int t) throws IOException {
		if (lookahead == t)  {
			lookahead = System.in.read();
			cnt++;
		}
		else  throw new Error("Syntax Error");
	}
	/**
	 * errPosition()用来记录出错的具体位置信息以及其对应的字符。
	 * @return String 记录错误信息的字符串
	 */
	private String errPosition() {
		return " in column " + cnt + " => \'" + (char)lookahead + "\': ";
	}
}

/**
 * PanicPostfix用来将中缀表达式转换为对应的后缀表达式，它通过调用PanicParser类来实现。
 * <p>
 * 它会捕捉PanicParser丢出的错误信息并将其输出，输出的内容包括：
 * <ul>
 * <li>错误的类型：Syntax Error， Lexical Error
 * <li>出错的位置
 * </ul>
 * 
 * @author Donald
 * @see PanicParser
 */
public class PanicPostfix {
	/**
	 * main函数，程序的入口。
	 * 通过调用PanicParser类来实现中缀转后缀，并处理相应的错误信息
	 * @param args 程序的输入参数
	 * @throws IOException IO流异常
	 * @see PanicParser
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Input an infix expression and output its postfix notation:");
		try {
			new PanicParser().expr();
			if (PanicParser.errors.size() > 0) {
				System.out.print("\n");
				for (Error error: PanicParser.errors) {
					System.out.println(error.getMessage());
				}
			}
		}catch (Error ie) {
			System.out.println("Fatal Error! " + ie.getMessage());
		}finally {
			//do nothing
		}
		System.out.println("\nEnd of program.");
	}
}


