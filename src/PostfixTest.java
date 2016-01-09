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

	//使用System.setOut将原本输出到终端的System.in流转存到outContent变量中，以便后面的测试判断
	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	}
	
	//每个Test执行完毕之后将setOut内容清空
	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	}
	
	//用于后面的Exception的判断
    @Rule
    public ExpectedException thrown= ExpectedException.none();
	
    //正确的测试样例1，当得到的结果与期望的结果一致时，assert正确
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
	
	//正确的测试样例2
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
	
	//语法错误测试，当出现语法错误异常，抛出Syntax Error信息时，assert正确
	@Test
	public void testSyntaxErr() throws IOException {
		thrown.expect(Error.class);
		thrown.expectMessage("Syntax Error! Right computation is needed.");
		String infixData = "9-5+-2";
		System.setIn(new ByteArrayInputStream(infixData.getBytes()));
		new Parser().expr();
	}
	
	//词法错误测试，当出现词法错误异常，抛出Lexical Error信息时，assert正确
	@Test
	public void testLexicalErr() throws IOException {
		thrown.expect(Error.class);
		thrown.expectMessage("Lexical Error! The number should only from 0~9.");
		String infixData = "95+2";
		System.setIn(new ByteArrayInputStream(infixData.getBytes()));
		new Parser().expr();
	}
	
	//词法错误测试2，当出现词法错误异常，抛出Lexical Error信息时，assert正确
	@Test
	public void testLexicalErr2() throws IOException {
		thrown.expect(Error.class);
		thrown.expectMessage("Lexical Error! The operator can only be + or -.");
		String infixData = "9$2";
		System.setIn(new ByteArrayInputStream(infixData.getBytes()));
		new Parser().expr();
	}
 	
}
