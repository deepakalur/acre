/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AAdditiveExprTmpRelationalExpr extends PTmpRelationalExpr
{
    private PAdditiveExpr _additiveExpr_;

    public AAdditiveExprTmpRelationalExpr()
    {
    }

    public AAdditiveExprTmpRelationalExpr(
        PAdditiveExpr _additiveExpr_)
    {
        setAdditiveExpr(_additiveExpr_);

    }
    public Object clone()
    {
        return new AAdditiveExprTmpRelationalExpr(
            (PAdditiveExpr) cloneNode(_additiveExpr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAAdditiveExprTmpRelationalExpr(this);
    }

    public PAdditiveExpr getAdditiveExpr()
    {
        return _additiveExpr_;
    }

    public void setAdditiveExpr(PAdditiveExpr node)
    {
        if(_additiveExpr_ != null)
        {
            _additiveExpr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _additiveExpr_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_additiveExpr_);
    }

    void removeChild(Node child)
    {
        if(_additiveExpr_ == child)
        {
            _additiveExpr_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_additiveExpr_ == oldChild)
        {
            setAdditiveExpr((PAdditiveExpr) newChild);
            return;
        }

    }
}