/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AInExpr extends PExpr
{
    private PExpr _left_;
    private TNot _not_;
    private TIn _in_;
    private final LinkedList _right_ = new TypedLinkedList(new Right_Cast());

    public AInExpr()
    {
    }

    public AInExpr(
        PExpr _left_,
        TNot _not_,
        TIn _in_,
        List _right_)
    {
        setLeft(_left_);

        setNot(_not_);

        setIn(_in_);

        {
            this._right_.clear();
            this._right_.addAll(_right_);
        }

    }

    public AInExpr(
        PExpr _left_,
        TNot _not_,
        TIn _in_,
        XPExpr _right_)
    {
        setLeft(_left_);

        setNot(_not_);

        setIn(_in_);

        if(_right_ != null)
        {
            while(_right_ instanceof X1PExpr)
            {
                this._right_.addFirst(((X1PExpr) _right_).getPExpr());
                _right_ = ((X1PExpr) _right_).getXPExpr();
            }
            this._right_.addFirst(((X2PExpr) _right_).getPExpr());
        }

    }
    public Object clone()
    {
        return new AInExpr(
            (PExpr) cloneNode(_left_),
            (TNot) cloneNode(_not_),
            (TIn) cloneNode(_in_),
            cloneList(_right_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAInExpr(this);
    }

    public PExpr getLeft()
    {
        return _left_;
    }

    public void setLeft(PExpr node)
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

    public LinkedList getRight()
    {
        return _right_;
    }

    public void setRight(List list)
    {
        _right_.clear();
        _right_.addAll(list);
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

        if(_right_.remove(child))
        {
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_left_ == oldChild)
        {
            setLeft((PExpr) newChild);
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

        for(ListIterator i = _right_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set(newChild);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

    }

    private class Right_Cast implements Cast
    {
        public Object cast(Object o)
        {
            PExpr node = (PExpr) o;

            if((node.parent() != null) &&
                (node.parent() != AInExpr.this))
            {
                node.parent().removeChild(node);
            }

            if((node.parent() == null) ||
                (node.parent() != AInExpr.this))
            {
                node.parent(AInExpr.this);
            }

            return node;
        }
    }
}