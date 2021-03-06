/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ATmpValueList extends PValueList
{
    private PValueList _valueList_;
    private TComma _comma_;
    private PExprRestricted _exprRestricted_;

    public ATmpValueList()
    {
    }

    public ATmpValueList(
        PValueList _valueList_,
        TComma _comma_,
        PExprRestricted _exprRestricted_)
    {
        setValueList(_valueList_);

        setComma(_comma_);

        setExprRestricted(_exprRestricted_);

    }
    public Object clone()
    {
        return new ATmpValueList(
            (PValueList) cloneNode(_valueList_),
            (TComma) cloneNode(_comma_),
            (PExprRestricted) cloneNode(_exprRestricted_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseATmpValueList(this);
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

    public PExprRestricted getExprRestricted()
    {
        return _exprRestricted_;
    }

    public void setExprRestricted(PExprRestricted node)
    {
        if(_exprRestricted_ != null)
        {
            _exprRestricted_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _exprRestricted_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_valueList_)
            + toString(_comma_)
            + toString(_exprRestricted_);
    }

    void removeChild(Node child)
    {
        if(_valueList_ == child)
        {
            _valueList_ = null;
            return;
        }

        if(_comma_ == child)
        {
            _comma_ = null;
            return;
        }

        if(_exprRestricted_ == child)
        {
            _exprRestricted_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_valueList_ == oldChild)
        {
            setValueList((PValueList) newChild);
            return;
        }

        if(_comma_ == oldChild)
        {
            setComma((TComma) newChild);
            return;
        }

        if(_exprRestricted_ == oldChild)
        {
            setExprRestricted((PExprRestricted) newChild);
            return;
        }

    }
}
