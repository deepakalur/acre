/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ANewListExpr extends PExpr
{
    private TList _list_;
    private TBracketL _bracketL_;
    private PValueOrRange _valueOrRange_;
    private TBracketR _bracketR_;

    public ANewListExpr()
    {
    }

    public ANewListExpr(
        TList _list_,
        TBracketL _bracketL_,
        PValueOrRange _valueOrRange_,
        TBracketR _bracketR_)
    {
        setList(_list_);

        setBracketL(_bracketL_);

        setValueOrRange(_valueOrRange_);

        setBracketR(_bracketR_);

    }
    public Object clone()
    {
        return new ANewListExpr(
            (TList) cloneNode(_list_),
            (TBracketL) cloneNode(_bracketL_),
            (PValueOrRange) cloneNode(_valueOrRange_),
            (TBracketR) cloneNode(_bracketR_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseANewListExpr(this);
    }

    public TList getList()
    {
        return _list_;
    }

    public void setList(TList node)
    {
        if(_list_ != null)
        {
            _list_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _list_ = node;
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

    public PValueOrRange getValueOrRange()
    {
        return _valueOrRange_;
    }

    public void setValueOrRange(PValueOrRange node)
    {
        if(_valueOrRange_ != null)
        {
            _valueOrRange_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _valueOrRange_ = node;
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
            + toString(_list_)
            + toString(_bracketL_)
            + toString(_valueOrRange_)
            + toString(_bracketR_);
    }

    void removeChild(Node child)
    {
        if(_list_ == child)
        {
            _list_ = null;
            return;
        }

        if(_bracketL_ == child)
        {
            _bracketL_ = null;
            return;
        }

        if(_valueOrRange_ == child)
        {
            _valueOrRange_ = null;
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
        if(_list_ == oldChild)
        {
            setList((TList) newChild);
            return;
        }

        if(_bracketL_ == oldChild)
        {
            setBracketL((TBracketL) newChild);
            return;
        }

        if(_valueOrRange_ == oldChild)
        {
            setValueOrRange((PValueOrRange) newChild);
            return;
        }

        if(_bracketR_ == oldChild)
        {
            setBracketR((TBracketR) newChild);
            return;
        }

    }
}
