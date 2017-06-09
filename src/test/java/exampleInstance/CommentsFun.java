package exampleInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentsFun {

static List<Match> commentMatches = new ArrayList<Match>();

public static void main(String[] args) {
	MatchComment();
	//String clean = original.replaceAll( "//.*|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/", "$1 " );
    Pattern commentsPattern = Pattern.compile("(//.*?$)|(/\\*.*?\\*/)", Pattern.MULTILINE | Pattern.DOTALL);
    Pattern stringsPattern = Pattern.compile("(\".*?(?<!\\\\)\")");

    //String text = getTextFromFile("src/my/test/CommentsFun.java");
    String text = "int/* some comment */foo = 5;";
    Matcher commentsMatcher = commentsPattern.matcher(text);
    while (commentsMatcher.find()) {
        Match match = new Match();
        match.start = commentsMatcher.start();
        match.text = commentsMatcher.group();
        commentMatches.add(match);
    }

    List<Match> commentsToRemove = new ArrayList<Match>();

    Matcher stringsMatcher = stringsPattern.matcher(text);
    while (stringsMatcher.find()) {
        for (Match comment : commentMatches) {
            if (comment.start > stringsMatcher.start() && comment.start < stringsMatcher.end())
                commentsToRemove.add(comment);
        }
    }
    for (Match comment : commentsToRemove)
        commentMatches.remove(comment);

    for (Match comment : commentMatches)
        text = text.replace(comment.text, " ");

    System.out.println(text);
}

public static void MatchComment() {
    String subjectString = "out1 (* c1 *) out2 (* c2 (* c3 *) c2 *) out3";
    String regex = "" +
        "# Match an innermost pascal '(*...*)' style comment.\n" +
        "\\(\\*      # Comment opening literal delimiter.\n" +
        "[^(*]*      # {normal*} Zero or more non'(', non-'*'.\n" +
        "(?:         # Begin {(special normal*)*} construct.\n" +
        "  (?!       # If we are not at the start of either...\n" +
        "    \\(\\*  # a nested comment\n" +
        "  | \\*\\)  # or the end of this comment,\n" +
        "  ) [(*]    # then ok to match a '(' or '*'.\n" +
        "  [^(*]*    # more {normal*}.\n" +
        ")*          # end {(special normal*)*} construct.\n" +
        "\\*\\)      # Comment closing literal delimiter.";
    String resultString = null;
    java.util.regex.Pattern p = java.util.regex.Pattern.compile(
                regex,
                java.util.regex.Pattern.COMMENTS);
    java.util.regex.Matcher m = p.matcher(subjectString);
    while (m.find())
    { // Iterate until there are no more "(* comments *)".
        resultString = m.replaceAll("");
        m = p.matcher(resultString);
    }
    System.out.println(resultString);
}


//Single-line

// "String? Nope"

/*
* "This  is not String either"
*/

//Complex */
///*More complex*/

/*Single line, but */

String moreFun = " /* comment? doubt that */";

String evenMoreFun = " // comment? doubt that ";

static class Match {
    int start;
    String text;
}
}