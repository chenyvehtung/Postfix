import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PostfixTest {
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	//ʹ��System.setOut��ԭ��������ն˵�System.in��ת�浽outContent�����У��Ա����Ĳ����ж�
	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	}
	
	//ÿ��Testִ�����֮��setOut�������
	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	}
	
	//���ں����Exception���ж�
    @Rule
    public ExpectedException thrown= ExpectedException.none();
	
    //��ȷ�Ĳ�������1�����õ��Ľ���������Ľ��һ��ʱ��assert��ȷ
	@Test
	public void testNormal() {
		String infixData = "9-5+2";
		String postfixData = "95-2+";
		try {
			System.setIn(new ByteArrayInputStream(infixData.getBytes()));
			new Parser().expr();			
			assertEquals(postfixData, outContent.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//��ȷ�Ĳ�������2
	@Test
	public void testNormal2() {
		String infixData = "1-2+3-4+5-6+7-8+9-0";
		String postfixData = "12-3+4-5+6-7+8-9+0-";
		try {
			System.setIn(new ByteArrayInputStream(infixData.getBytes()));
			new Parser().expr();			
			assertEquals(postfixData, outContent.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//�﷨������ԣ��������﷨�����쳣���׳�Syntax Error��Ϣʱ��assert��ȷ
	@Test
	public void testSyntaxErr() throws IOException {
		thrown.expect(Error.class);
		thrown.expectMessage("Syntax Error! Right computation is needed.");
		String infixData = "9-5+-2";
		System.setIn(new ByteArrayInputStream(infixData.getBytes()));
		new Parser().expr();
	}
	
	//�ʷ�������ԣ������ִʷ������쳣���׳�Lexical Error��Ϣʱ��assert��ȷ
	@Test
	public void testLexicalErr() throws IOException {
		thrown.expect(Error.class);
		thrown.expectMessage("Lexical Error! The number should only from 0~9.");
		String infixData = "95+2";
		System.setIn(new ByteArrayInputStream(infixData.getBytes()));
		new Parser().expr();
	}
	
	//�ʷ��������2�������ִʷ������쳣���׳�Lexical Error��Ϣʱ��assert��ȷ
	@Test
	public void testLexicalErr2() throws IOException {
		thrown.expect(Error.class);
		thrown.expectMessage("Lexical Error! The operator can only be + or -.");
		String infixData = "9$2";
		System.setIn(new ByteArrayInputStream(infixData.getBytes()));
		new Parser().expr();
	}
 	
}
