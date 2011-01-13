/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class APostfixUnaryExpr extends PUnaryExpr
{
    private PPostfixExpr _postfixExpr_;

    public APostfixUnaryExpr()
    {
    }

    public APostfixUnaryExpr(
        PPostfixExpr _postfixExpr_)
    {
        setPostfixExpr(_postfixExpr_);

    }
    public Object clone()
    {
        return new APostfixUnaryExpr(
            (PPostfixExpr) cloneNode(_postfixExpr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAPostfixUnaryExpr(this);
    }

    public PPostfixExpr getPostfixExpr()
    {
        return _postfixExpr_;
    }

    public void setPostfixExpr(PPostfixExpr node)
    {
        if(_postfixExpr_ != null)
        {
            _postfixExpr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _postfixExpr_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_postfixExpr_);
    }

    void removeChild(Node child)
    {
        if(_postfixExpr_ == child)
        {
            _postfixExpr_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_postfixExpr_ == oldChild)
        {
            setPostfixExpr((PPostfixExpr) newChild);
            return;
        }

    }
}
