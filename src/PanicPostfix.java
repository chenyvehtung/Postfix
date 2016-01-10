import java.io.*;
import java.util.ArrayList;

/**
 * PanicParser��һ���򵥵Ĵʷ������������Ļ��������DBv2��2���ṩ�������ܵ�������һ����׺���ʽ���Ҹ���׺���ʽ������ѭ���¹���
 * <ul>
 * <li>���ʽ�е�������ֻ����0~9�ĵ�������
 * <li>���ʽ�е������ֻ�ܰ�����('+')����('-')
 * <li>���ʽ�в������κοո��Ʊ���ȿհ׷���
 * </ul>
 * �������������һ����׺���ʽ��������ʽ�����д��������¼�������ͣ�������
 * <ul>
 * <li>Syntax Error �﷨����
 * <li>Lexical Error �ʷ�����
 * </ul>
 * ���⣬��������ÿֻ�ģʽ��panic mode����error���лָ������ո���һ����ȷ�ĺ�׺���ʽ�Լ���Ӧ�ĳ���λ�ú���Ϣ
 * @author Donald
 *
 */
class PanicParser {
	/**
	 * ��̬��Ա������������ȡ��������һλ�ַ�
	 */
	static int lookahead;
	/**
	 * ˽�г�Ա������������¼��ǰ�ַ���λ��
	 */
	private int cnt;
	/**
	 * ˽�г�Ա������������¼���ʽ�Ƿ��Ѿ�ƥ�䵽��һ�����֡�
	 */
	private Boolean started = false;
	/**
	 * ��̬��Ա���飬������¼������Ϣ��
	 */
	static ArrayList<Error> errors = new ArrayList<Error>();
	/**
	 * PanicParser��Ĺ��캯����
	 * �����ȡһ���ַ����Ҵ洢��lookahead�����С�
	 * 
	 * @throws IOException IO���쳣
	 * @see lookahead
	 */
	public PanicParser() throws IOException {
		lookahead = System.in.read();
		cnt = 1;
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
	 * rest()��PanicParser��������ĵĺ�����
	 * ��ƥ�䡮+������-�����ţ�Ȼ���ȡ��һ���ַ�����ӡ�������������ٽ��������ӡ����������ʵ������׺ת��׺��
	 * ����ѭ��ִ�У�ֱ��������ִ���������������<br>
	 * ���ִ���ʱ����¼�ʷ�������Ϣ�������ÿֻ�ģʽһֱ����ȡ��ֱ����ȡ��һ����ȷ�ķ���Ϊֹ��
	 * 
	 * @throws IOException IO���쳣
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
				errors.add(new Error("Lexical Error" + errPosition() + "Expression only supports Unit."));
			else
				errors.add(new Error("Lexical Error" + errPosition() + "The operator can only be + or -."));
			match(lookahead);
			rest();
		}
	}
	/**
	 * term()ƥ����������
	 * ��������������ͻ������ȡ��һ���ַ��������¼�﷨������Ϣ����һֱ����ȡ��ֱ����ȡ��һ������Ϊֹ��
	 * 
	 * @throws IOException IO���쳣
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
	/**
	 * errPosition()������¼����ľ���λ����Ϣ�Լ����Ӧ���ַ���
	 * @return String ��¼������Ϣ���ַ���
	 */
	private String errPosition() {
		return " in column " + cnt + " => \'" + (char)lookahead + "\': ";
	}
}

/**
 * PanicPostfix��������׺���ʽת��Ϊ��Ӧ�ĺ�׺���ʽ����ͨ������PanicParser����ʵ�֡�
 * <p>
 * ���Ჶ׽PanicParser�����Ĵ�����Ϣ�������������������ݰ�����
 * <ul>
 * <li>��������ͣ�Syntax Error�� Lexical Error
 * <li>�����λ��
 * </ul>
 * 
 * @author Donald
 * @see PanicParser
 */
public class PanicPostfix {
	/**
	 * main�������������ڡ�
	 * ͨ������PanicParser����ʵ����׺ת��׺����������Ӧ�Ĵ�����Ϣ
	 * @param args ������������
	 * @throws IOException IO���쳣
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


