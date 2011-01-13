/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AIdentifierMethodPostfixExpr extends PPostfixExpr
{
    private TIdentifier _left_;
    private PDotarrow _dotarrow_;
    private TIdentifier _right_;
    private TBracketL _bracketL_;
    private PValueList _valueList_;
    private TBracketR _bracketR_;

    public AIdentifierMethodPostfixExpr()
    {
    }

    public AIdentifierMethodPostfixExpr(
        TIdentifier _left_,
        PDotarrow _dotarrow_,
        TIdentifier _right_,
        TBracketL _bracketL_,
        PValueList _valueList_,
        TBracketR _bracketR_)
    {
        setLeft(_left_);

        setDotarrow(_dotarrow_);

        setRight(_right_);

        setBracketL(_bracketL_);

        setValueList(_valueList_);

        setBracketR(_bracketR_);

    }
    public Object clone()
    {
        return new AIdentifierMethodPostfixExpr(
            (TIdentifier) cloneNode(_left_),
            (PDotarrow) cloneNode(_dotarrow_),
            (TIdentifier) cloneNode(_right_),
            (TBracketL) cloneNode(_bracketL_),
            (PValueList) cloneNode(_valueList_),
            (TBracketR) cloneNode(_bracketR_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAIdentifierMethodPostfixExpr(this);
    }

    public TIdentifier getLeft()
    {
        return _left_;
    }

    public void setLeft(TIdentifier node)
    {
        if(_left_ != null)
        {
            _left_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _left_ = node;
    }

    public PDotarrow getDotarrow()
    {
        return _dotarrow_;
    }

    public void setDotarrow(PDotarrow node)
    {
        if(_dotarrow_ != null)
        {
            _dotarrow_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _dotarrow_ = node;
    }

    public TIdentifier getRight()
    {
        return _right_;
    }

    public void setRight(TIdentifier node)
    {
        if(_right_ != null)
        {
            _right_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _right_ = node;
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

    public PValueList getValueList()
    {
        return _valueList_;
    }

    public void setValueList(PValueList node)
    {
        if(_valueList_ != null)
        {
            _valueList_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _valueList_ = node;
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
            + toString(_left_)
            + toString(_dotarrow_)
            + toString(_right_)
            + toString(_bracketL_)
            + toString(_valueList_)
            + toString(_bracketR_);
    }

    void removeChild(Node child)
    {
        if(_left_ == child)
        {
            _left_ = null;
            return;
        }

        if(_dotarrow_ == child)
        {
            _dotarrow_ = null;
            return;
        }

        if(_right_ == child)
        {
            _right_ = null;
            return;
        }

        if(_bracketL_ == child)
        {
            _bracketL_ = null;
            return;
        }

        if(_valueList_ == child)
        {
            _valueList_ = null;
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
        if(_left_ == oldChild)
        {
            setLeft((TIdentifier) newChild);
            return;
        }

        if(_dotarrow_ == oldChild)
        {
            setDotarrow((PDotarrow) newChild);
            return;
        }

        if(_right_ == oldChild)
        {
            setRight((TIdentifier) newChild);
            return;
        }

        if(_bracketL_ == oldChild)
        {
            setBracketL((TBracketL) newChild);
            return;
        }

        if(_valueList_ == oldChild)
        {
            setValueList((PValueList) newChild);
            return;
        }

        if(_bracketR_ == oldChild)
        {
            setBracketR((TBracketR) newChild);
            return;
        }

    }
}