/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ANewStructExpr extends PExpr
{
    private TStruct _struct_;
    private TBracketL _bracketL_;
    private PFieldList _fieldList_;
    private TBracketR _bracketR_;

    public ANewStructExpr()
    {
    }

    public ANewStructExpr(
        TStruct _struct_,
        TBracketL _bracketL_,
        PFieldList _fieldList_,
        TBracketR _bracketR_)
    {
        setStruct(_struct_);

        setBracketL(_bracketL_);

        setFieldList(_fieldList_);

        setBracketR(_bracketR_);

    }
    public Object clone()
    {
        return new ANewStructExpr(
            (TStruct) cloneNode(_struct_),
            (TBracketL) cloneNode(_bracketL_),
            (PFieldList) cloneNode(_fieldList_),
            (TBracketR) cloneNode(_bracketR_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseANewStructExpr(this);
    }

    public TStruct getStruct()
    {
        return _struct_;
    }

    public void setStruct(TStruct node)
    {
        if(_struct_ != null)
        {
            _struct_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _struct_ = node;
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
            + toString(_struct_)
            + toString(_bracketL_)
            + toString(_fieldList_)
            + toString(_bracketR_);
    }

    void removeChild(Node child)
    {
        if(_struct_ == child)
        {
            _struct_ = null;
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
        if(_struct_ == oldChild)
        {
            setStruct((TStruct) newChild);
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
