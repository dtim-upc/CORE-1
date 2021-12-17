package edu.puc.core2.parser.exceptions;

import org.antlr.v4.runtime.ParserRuleContext;

public class TypeException extends ParserException {
    public TypeException(String msg, ParserRuleContext context) {
        super(msg, context);
    }
}