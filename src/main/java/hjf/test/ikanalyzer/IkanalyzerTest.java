package hjf.test.ikanalyzer;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;

/**
 * 中文分词器
 * Created by javen on 2017/7/17.
 */
public class IkanalyzerTest {

    public static void main(String[] args) {
        String str = "测试分词器，Where are you from";
        IKSegmenter ikSegmenter = new IKSegmenter(new StringReader(str), false);
        Lexeme lex = null;
        try {
            while ((lex = ikSegmenter.next()) != null) {
                System.out.println(lex.getLexemeText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
