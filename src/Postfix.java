import java.io.*;

/**
 * Parser��һ���򵥵Ĵʷ������������Ļ��������DBv2��2���ṩ�������ܵ�������һ����׺���ʽ���Ҹ���׺���ʽ������ѭ���¹���
 * <ul>
 * <li>���ʽ�е�������ֻ����0~9�ĵ�������
 * <li>���ʽ�е������ֻ�ܰ�����('+')����('-')
 * <li>���ʽ�в������κοո��Ʊ���ȿհ׷���
 * </ul>
 * �������������һ����׺���ʽ��������ʽ�����д��������׳��������ͣ�������
 * <ul>
 * <li>Syntax Error �﷨����
 * <li>Lexical Error �ʷ�����
 * </ul>
 * 
 * @author Donald
 *
 */
class Parser {
	/**
	 * ��̬��Ա������������ȡ��������һλ�ַ�
	 */
	static int lookahead;
	/**
	 * ��̬��Ա������������¼��ǰ�ַ���λ��
	 */
	static int cnt;
	/**
	 * Parser��Ĺ��캯����
	 * �����ȡһ���ַ����Ҵ洢��lookahead�����С�
	 * 
	 * @throws IOException IO���쳣
	 * @see lookahead
	 */
	public Parser() throws IOException {
		lookahead = System.in.read();
	}
	/**
	 * expr()��ȡ��׺���ʽ������Ӧ�ĺ�׺���ʽ��ӡ������ͨ�����ú���term()��rest()��ʵ�֡�
	 * 
	 * @throws IOException IO���쳣
	 * @see #term()
	 * @see #rest()
	 */
	void expr() throws IOException {
		term();
		rest();
	}
	/**
	 * rest()��Parser��������ĵĺ�����
	 * ��ƥ�䡮+������-�����ţ�Ȼ���ȡ��һ���ַ�����ӡ�������������ٽ��������ӡ����������ʵ������׺ת��׺��
	 * ����ѭ��ִ�У�ֱ��������ִ������������������ִ���ʱ���׳��ʷ�������Ϣ
	 * 
	 * @throws IOException IO���쳣
	 * @throws Error �ʷ�����(Lexical Error)�쳣
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
		//13�ǻس��������ն��ֶ�����ʱ�����һ���ַ���-1�ǲ���ʱ�õ����ַ�����������һ���ַ�
		if ( !(lookahead == 13 || lookahead == -1)) {
			if (Character.isDigit((char)lookahead))
				throw new Error("Lexical Error! The number should only from 0~9.");
			else
				throw new Error("Lexical Error! The operator can only be + or -.");
		}
	}
	/**
	 * term()ƥ����������
	 * ��������������ͻ������ȡ��һ���ַ��������׳��﷨������Ϣ��
	 * 
	 * @throws IOException IO���쳣
	 * @throws Error �﷨����(Syntax Error)�쳣
	 * @see #match(int)
	 */
	void term() throws IOException {
		if (Character.isDigit((char)lookahead)) {
			System.out.write((char)lookahead);
			match(lookahead);
		} else  throw new Error("Syntax Error! Right computation is needed.");
	}
	/**
	 * match()ƥ��lookahead��
	 * ���lookahead��ͬ���Ͷ�ȡ��һ���ַ���������lookahead��ͬʱ��cnt����һ�������׳��﷨������Ϣ��
	 * 
	 * @param t �ַ���������lookahead���бȶ�
	 * @throws IOException IO���쳣
	 * @throws Error �﷨����(Syntax Error)�쳣
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
 * Postfix��������׺���ʽת��Ϊ��Ӧ�ĺ�׺���ʽ����ͨ������Parser����ʵ�֡�
 * <p>
 * ���Ჶ׽Parser�����Ĵ�����Ϣ�������������������ݰ�����
 * <ul>
 * <li>��������ͣ�Syntax Error�� Lexical Error
 * <li>�����λ��
 * </ul>
 * 
 * @author Donald
 * @see Parser
 */
public class Postfix {
	/**
	 * main�������������ڡ�
	 * ͨ������Parser����ʵ����׺ת��׺����������Ӧ�Ĵ�����Ϣ
	 * @param args ������������
	 * @throws IOException IO���쳣
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
