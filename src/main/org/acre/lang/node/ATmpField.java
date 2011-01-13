/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ATmpField extends PField
{
    private TIdentifier _identifier_;
    private TColon _colon_;
    private PExprRestricted _exprRestricted_;

    public ATmpField()
    {
    }

    public ATmpField(
        TIdentifier _identifier_,
        TColon _colon_,
        PExprRestricted _exprRestricted_)
    {
        setIdentifier(_identifier_);

        setColon(_colon_);

        setExprRestricted(_exprRestricted_);

    }
    public Object clone()
    {
        return new ATmpField(
            (TIdentifier) cloneNode(_identifier_),
            (TColon) cloneNode(_colon_),
            (PExprRestricted) cloneNode(_exprRestricted_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseATmpField(this);
    }

    public TIdentifier getIdentifier()
    {
        return _identifier_;
    }

    public void setIdentifier(TIdentifier node)
    {
        if(_identifier_ != null)
        {
            _identifier_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _identifier_ = node;
    }

    public TColon getColon()
    {
        return _colon_;
    }

    public void setColon(TColon node)
    {
        if(_colon_ != null)
        {
            _colon_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _colon_ = node;
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
            + toString(_identifier_)
            + toString(_colon_)
            + toString(_exprRestricted_);
    }

    void removeChild(Node child)
    {
        if(_identifier_ == child)
        {
            _identifier_ = null;
            return;
        }

        if(_colon_ == child)
        {
            _colon_ = null;
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
        if(_identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
            return;
        }

        if(_colon_ == oldChild)
        {
            setColon((TColon) newChild);
            return;
        }

        if(_exprRestricted_ == oldChild)
        {
            setExprRestricted((PExprRestricted) newChild);
            return;
        }

    }
}