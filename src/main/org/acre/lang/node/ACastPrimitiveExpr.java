/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ACastPrimitiveExpr extends PExpr
{
    private TBracketL _bracketL_;
    private PType _type_;
    private TBracketR _bracketR_;
    private PExpr _expr_;

    public ACastPrimitiveExpr()
    {
    }

    public ACastPrimitiveExpr(
        TBracketL _bracketL_,
        PType _type_,
        TBracketR _bracketR_,
        PExpr _expr_)
    {
        setBracketL(_bracketL_);

        setType(_type_);

        setBracketR(_bracketR_);

        setExpr(_expr_);

    }
    public Object clone()
    {
        return new ACastPrimitiveExpr(
            (TBracketL) cloneNode(_bracketL_),
            (PType) cloneNode(_type_),
            (TBracketR) cloneNode(_bracketR_),
            (PExpr) cloneNode(_expr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseACastPrimitiveExpr(this);
    }

    public TBracketL getBracketL()
    {
        return _bracketL_;
    }

    public void setBracketL(TBracketL node)
    {
        if(_bracketL_ != null)
        {
            _bracketL_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _bracketL_ = node;
    }

    public PType getType()
    {
        return _type_;
    }

    public void setType(PType node)
    {
        if(_type_ != null)
        {
            _type_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _type_ = node;
    }

    public TBracketR getBracketR()
    {
        return _bracketR_;
    }

    public void setBracketR(TBracketR node)
    {
        if(_bracketR_ != null)
        {
            _bracketR_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _bracketR_ = node;
    }

    public PExpr getExpr()
    {
        return _expr_;
    }

    public void setExpr(PExpr node)
    {
        if(_expr_ != null)
        {
            _expr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _expr_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_bracketL_)
            + toString(_type_)
            + toString(_bracketR_)
            + toString(_expr_);
    }

    void removeChild(Node child)
    {
        if(_bracketL_ == child)
        {
            _bracketL_ = null;
            return;
        }

        if(_type_ == child)
        {
            _type_ = null;
            return;
        }

        if(_bracketR_ == child)
        {
            _bracketR_ = null;
            return;
        }

        if(_expr_ == child)
        {
            _expr_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_bracketL_ == oldChild)
        {
            setBracketL((TBracketL) newChild);
            return;
        }

        if(_type_ == oldChild)
        {
            setType((PType) newChild);
            return;
        }

        if(_bracketR_ == oldChild)
        {
            setBracketR((TBracketR) newChild);
            return;
        }

        if(_expr_ == oldChild)
        {
            setExpr((PExpr) newChild);
            return;
        }

    }
}