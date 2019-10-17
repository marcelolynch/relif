package ar.edu.itba.relif.application.view;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.EditableStyledDocument;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RelifCodeArea extends CodeArea {

    private static final String[] KEYWORDS = new String[] {
            "iden","univ","for", "rel",",", "=" ,"in" ,"\\+" ,"-" ,"\\." ,"~",
            "&" ,"no" ,"some" ,"!" ,"not" ,"&&" ,"and" ,"\\|\\|" ,
            "or" ,"=>","implies" ,"<=>" ,"iff" , "run" ,"check",
            "id" ,"sym" ,"asym" ,"but", "default"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String COMMENT_PATTERN = "(?m)//.*$" + "|" + "/\\*(.|\\R)*?\\*/";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<COMMENT>" + COMMENT_PATTERN + ")" +
            "|(?<KEYWORD>" + KEYWORD_PATTERN + ")"
    );

    public RelifCodeArea() {
        super();
        setParagraphGraphicFactory(LineNumberFactory.get(this));
        multiPlainChanges().successionEnds(Duration.ofMillis(50))
                .subscribe(__ -> setStyleSpans(0, computeHighlighting(getText())));
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                                    matcher.group("COMMENT") != null ? "comment" :
                                    ""; /* never happens */ assert !styleClass.equals("");
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

}
