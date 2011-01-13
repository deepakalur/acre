/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ABothTmpInExpr extends PTmpInExpr
{
    private TIdentifier _left_;
    private TNot _not_;
    private TIn _in_;
    private TIdentifier _right_;

    public ABothTmpInExpr()
    {
    }

    public ABothTmpInExpr(
        TIdentifier _left_,
        TNot _not_,
        TIn _in_,
        TIdentifier _right_)
    {
        setLeft(_left_);

        setNot(_not_);

        setIn(_in_);

        setRight(_right_);

    }
    public Object clone()
    {
        return new ABothTmpInExpr(
            (TIdentifier) cloneNode(_left_),
            (TNot) cloneNode(_not_),
            (TIn) cloneNode(_in_),
            (TIdentifier) cloneNode(_right_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseABothTmpInExpr(this);
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

    public TNot getNot()
    {
        return _not_;
    }

    public void setNot(TNot node)
    {
        if(_not_ != null)
        {
            _not_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _not_ = node;
    }

    public TIn getIn()
    {
        return _in_;
    }

    public void setIn(TIn node)
    {
        if(_in_ != null)
        {
            _in_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _in_ = node;
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

    public String toString()
    {
        return ""
            + toString(_left_)
            + toString(_not_)
            + toString(_in_)
            + toString(_right_);
    }

    void removeChild(Node child)
    {
        if(_left_ == child)
        {
            _left_ = null;
            return;
        }

        if(_not_ == child)
        {
            _not_ = null;
            return;
        }

        if(_in_ == child)
        {
            _in_ = null;
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
            setLeft((TIdentifier) newChild);
            return;
        }

        if(_not_ == oldChild)
        {
            setNot((TNot) newChild);
            return;
        }

        if(_in_ == oldChild)
        {
            setIn((TIn) newChild);
            return;
        }

        if(_right_ == oldChild)
        {
            setRight((TIdentifier) newChild);
            return;
        }

    }
}
