package edu.puc.core2.parser.exceptions;

import org.antlr.v4.runtime.ParserRuleContext;

public class UnknownDataTypeException extends ParserException {
    public UnknownDataTypeException(String msg, ParserRuleContext context) {
        super(msg, context);
    }
}
