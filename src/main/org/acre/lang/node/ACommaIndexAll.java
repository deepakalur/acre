/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ACommaIndexAll extends PIndexAll
{
    private PExprRestricted _left_;
    private TComma _comma_;
    private PExprRestricted _right_;

    public ACommaIndexAll()
    {
    }

    public ACommaIndexAll(
        PExprRestricted _left_,
        TComma _comma_,
        PExprRestricted _right_)
    {
        setLeft(_left_);

        setComma(_comma_);

        setRight(_right_);

    }
    public Object clone()
    {
        return new ACommaIndexAll(
            (PExprRestricted) cloneNode(_left_),
            (TComma) cloneNode(_comma_),
            (PExprRestricted) cloneNode(_right_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseACommaIndexAll(this);
    }

    public PExprRestricted getLeft()
    {
        return _left_;
    }

    public void setLeft(PExprRestricted node)
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

    public TComma getComma()
    {
        return _comma_;
    }

    public void setComma(TComma node)
    {
        if(_comma_ != null)
        {
            _comma_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _comma_ = node;
    }

    public PExprRestricted getRight()
    {
        return _right_;
    }

    public void setRight(PExprRestricted node)
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

    public String toString()
    {
        return ""
            + toString(_left_)
            + toString(_comma_)
            + toString(_right_);
    }

    void removeChild(Node child)
    {
        if(_left_ == child)
        {
            _left_ = null;
            return;
        }

        if(_comma_ == child)
        {
            _comma_ = null;
            return;
        }

        if(_right_ == child)
        {
            _right_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_left_ == oldChild)
        {
            setLeft((PExprRestricted) newChild);
            return;
        }

        if(_comma_ == oldChild)
        {
            setComma((TComma) newChild);
            return;
        }

        if(_right_ == oldChild)
        {
            setRight((PExprRestricted) newChild);
            return;
        }

    }
}
