import java.io.*;

/**
 * Parser是一个简单的词法分析器，它的基本框架由DBv2第2章提供。它接受的输入是一个中缀表达式，且该中缀表达式必须遵循以下规则：
 * <ul>
 * <li>表达式中的运算量只能是0~9的单个数字
 * <li>表达式中的运算符只能包含加('+')、减('-')
 * <li>表达式中不能有任何空格，制表符等空白符号
 * </ul>
 * 它的最终输出是一个后缀表达式，如果表达式输入有错误，它会抛出错误类型，包含：
 * <ul>
 * <li>Syntax Error 语法错误
 * <li>Lexical Error 词法错误
 * </ul>
 * 
 * @author Donald
 *
 */
class Parser {
	/**
	 * 静态成员变量，用来存取读到的下一位字符
	 */
	static int lookahead;
	/**
	 * 静态成员变量，用来记录当前字符的位置
	 */
	static int cnt;
	/**
	 * Parser类的构造函数。
	 * 它会读取一个字符并且存储到lookahead变量中。
	 * 
	 * @throws IOException IO流异常
	 * @see lookahead
	 */
	public Parser() throws IOException {
		lookahead = System.in.read();
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
	 * rest()是Parser类中最核心的函数。
	 * 它匹配‘+’，‘-’符号，然后读取下一个字符并打印出该运算量，再将运算符打印出，这样便实现了中缀转后缀。
	 * 它会循环执行，直到输入出现错误或者输入结束。出现错误时，抛出词法错误信息
	 * 
	 * @throws IOException IO流异常
	 * @throws Error 词法错误(Lexical Error)异常
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
				throw new Error("Lexical Error! The number should only from 0~9.");
			else
				throw new Error("Lexical Error! The operator can only be + or -.");
		}
	}
	/**
	 * term()匹配运算量。
	 * 如果是运算量，就会继续读取下一个字符。否则抛出语法错误信息。
	 * 
	 * @throws IOException IO流异常
	 * @throws Error 语法错误(Syntax Error)异常
	 * @see #match(int)
	 */
	void term() throws IOException {
		if (Character.isDigit((char)lookahead)) {
			System.out.write((char)lookahead);
			match(lookahead);
		} else  throw new Error("Syntax Error! Right computation is needed.");
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
}

/**
 * Postfix用来将中缀表达式转换为对应的后缀表达式，它通过调用Parser类来实现。
 * <p>
 * 它会捕捉Parser丢出的错误信息并将其输出，输出的内容包括：
 * <ul>
 * <li>错误的类型：Syntax Error， Lexical Error
 * <li>出错的位置
 * </ul>
 * 
 * @author Donald
 * @see Parser
 */
public class Postfix {
	/**
	 * main函数，程序的入口。
	 * 通过调用Parser类来实现中缀转后缀，并处理相应的错误信息
	 * @param args 程序的输入参数
	 * @throws IOException IO流异常
	 * @see Parser
	 * @see Postfix
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Input an infix expression and output its postfix notation:");
		try {
			new Parser().expr();
		}catch (Error ie) {
			System.out.println(" (error)\nThe error message is: "+ie.getMessage());  			
			System.out.println("The error happened at the " + (Parser.cnt+1) + 
					" Byte of the input, which is '" + (char)Parser.lookahead + "'");
			/*StackTraceElement[] stack = ie.getStackTrace();
			if (stack != null) {
				for (StackTraceElement stackTraceElement : stack) {
					System.out.println(stackTraceElement);
				}
			}*/
		}finally {
			//do nothing
		}
		System.out.println("\nEnd of program.");
	}
}
