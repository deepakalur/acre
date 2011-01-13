/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ANewObjectExpr extends PExpr
{
    private PExpr _expr_;
    private TBracketL _bracketL_;
    private PFieldList _fieldList_;
    private TBracketR _bracketR_;

    public ANewObjectExpr()
    {
    }

    public ANewObjectExpr(
        PExpr _expr_,
        TBracketL _bracketL_,
        PFieldList _fieldList_,
        TBracketR _bracketR_)
    {
        setExpr(_expr_);

        setBracketL(_bracketL_);

        setFieldList(_fieldList_);

        setBracketR(_bracketR_);

    }
    public Object clone()
    {
        return new ANewObjectExpr(
            (PExpr) cloneNode(_expr_),
            (TBracketL) cloneNode(_bracketL_),
            (PFieldList) cloneNode(_fieldList_),
            (TBracketR) cloneNode(_bracketR_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseANewObjectExpr(this);
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

    public PFieldList getFieldList()
    {
        return _fieldList_;
    }

    public void setFieldList(PFieldList node)
    {
        if(_fieldList_ != null)
        {
            _fieldList_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _fieldList_ = node;
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

    public String toString()
    {
        return ""
            + toString(_expr_)
            + toString(_bracketL_)
            + toString(_fieldList_)
            + toString(_bracketR_);
    }

    void removeChild(Node child)
    {
        if(_expr_ == child)
        {
            _expr_ = null;
            return;
        }

        if(_bracketL_ == child)
        {
            _bracketL_ = null;
            return;
        }

        if(_fieldList_ == child)
        {
            _fieldList_ = null;
            return;
        }

        if(_bracketR_ == child)
        {
            _bracketR_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_expr_ == oldChild)
        {
            setExpr((PExpr) newChild);
            return;
        }

        if(_bracketL_ == oldChild)
        {
            setBracketL((TBracketL) newChild);
            return;
        }

        if(_fieldList_ == oldChild)
        {
            setFieldList((PFieldList) newChild);
            return;
        }

        if(_bracketR_ == oldChild)
        {
            setBracketR((TBracketR) newChild);
            return;
        }

    }
}
