/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AConversionPrimaryExpr extends PPrimaryExpr
{
    private PConversionExpr _conversionExpr_;

    public AConversionPrimaryExpr()
    {
    }

    public AConversionPrimaryExpr(
        PConversionExpr _conversionExpr_)
    {
        setConversionExpr(_conversionExpr_);

    }
    public Object clone()
    {
        return new AConversionPrimaryExpr(
            (PConversionExpr) cloneNode(_conversionExpr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAConversionPrimaryExpr(this);
    }

    public PConversionExpr getConversionExpr()
    {
        return _conversionExpr_;
    }

    public void setConversionExpr(PConversionExpr node)
    {
        if(_conversionExpr_ != null)
        {
            _conversionExpr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _conversionExpr_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_conversionExpr_);
    }

    void removeChild(Node child)
    {
        if(_conversionExpr_ == child)
        {
            _conversionExpr_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_conversionExpr_ == oldChild)
        {
            setConversionExpr((PConversionExpr) newChild);
            return;
        }

    }
}
