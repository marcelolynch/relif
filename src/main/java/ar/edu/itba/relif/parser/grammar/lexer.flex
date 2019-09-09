package ar.edu.itba.relif.parser;
import java.io.*;
import java_cup.runtime.*;
import java_cup.runtime.ComplexSymbolFactory.Location;

%%
%unicode
%cup
%class Scanner
%{
	public Scanner(java.io.Reader r, ComplexSymbolFactory sf){
		this(r);
		this.sf=sf;
	}
	public Symbol symbol(String plaintext,int code){
	    return sf.newSymbol(plaintext, code, new Location("",yyline+1, yycolumn +1,yychar), new Location("",yyline+1,yycolumn+yylength(),yychar));
	}
	public Symbol symbol(String plaintext,int code, Object value){
	    return sf.newSymbol(plaintext,code,new Location("",yyline+1, yycolumn +1,yychar), new Location("",yyline+1,yycolumn+yylength(),yychar), value);
	}
	private ComplexSymbolFactory sf;
%}

%eofval{
    return sf.newSymbol("EOF", sym.EOF);
%eofval}


number = [0-9]+
ident = [A-Za-z][A-Za-z0-9]*
space = [\ \t]
newline = \r|\n|\r\n

%%

{newline} {}
{space} {}

"iden" { return symbol(yytext(), sym.IDEN); }
"univ" { return symbol(yytext(), sym.UNIV); }
"for" { return symbol(yytext(), sym.FOR); }

"(" { return symbol(yytext(), sym.OPENPAREN); }
")" { return symbol(yytext(), sym.CLOSEPAREN); }

"rel" { return symbol(yytext(), sym.REL); }
"," { return symbol(yytext(), sym.COMMA); }

"=" { return symbol(yytext(), sym.EQUALS); }
"in" { return symbol(yytext(), sym.IN); }

"+" { return symbol(yytext(), sym.PLUS); }
"-" { return symbol(yytext(), sym.MINUS); }
"." { return symbol(yytext(), sym.COMPOSE); }
"~" { return symbol(yytext(), sym.CONVERSE); }
"&" { return symbol(yytext(), sym.INTERSECTION); }

"no" { return symbol(yytext(), sym.NO); }
"some" { return symbol(yytext(), sym.SOME); }

"!" { return symbol(yytext(), sym.NOT); }
"not" { return symbol(yytext(), sym.NOT); }
"&&" { return symbol(yytext(), sym.AND); }
"and" { return symbol(yytext(), sym.AND); }
"||" { return symbol(yytext(), sym.OR); }
"or" { return symbol(yytext(), sym.OR); }
"=>" { return symbol(yytext(), sym.IMPLIES); }
"implies" { return symbol(yytext(), sym.IMPLIES); }
"<=>" { return symbol(yytext(), sym.IFF); }
"iff" { return symbol(yytext(), sym.IFF); }



"run" { return symbol(yytext(), sym.RUN); }
"id" { return symbol(yytext(), sym.IDENTITY); }
"sym" { return symbol(yytext(), sym.SYMMETRIC); }
"asym" { return symbol(yytext(), sym.ASYMMETRIC); }
"but" { return symbol(yytext(), sym.BUT); }
"default" { return symbol(yytext(), sym.DEFAULT); }


{ident} { String s = yytext(); return symbol(s, sym.IDENTIFIER, s); }
{number} { String s = yytext(); return symbol(s, sym.NUMBER, Integer.parseInt(s)); }