/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AIdentifierList extends PIdentifierList
{
    private PIdentifierList _identifierList_;
    private TComma _comma_;
    private TIdentifier _identifier_;

    public AIdentifierList()
    {
    }

    public AIdentifierList(
        PIdentifierList _identifierList_,
        TComma _comma_,
        TIdentifier _identifier_)
    {
        setIdentifierList(_identifierList_);

        setComma(_comma_);

        setIdentifier(_identifier_);

    }
    public Object clone()
    {
        return new AIdentifierList(
            (PIdentifierList) cloneNode(_identifierList_),
            (TComma) cloneNode(_comma_),
            (TIdentifier) cloneNode(_identifier_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAIdentifierList(this);
    }

    public PIdentifierList getIdentifierList()
    {
        return _identifierList_;
    }

    public void setIdentifierList(PIdentifierList node)
    {
        if(_identifierList_ != null)
        {
            _identifierList_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _identifierList_ = node;
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

    public String toString()
    {
        return ""
            + toString(_identifierList_)
            + toString(_comma_)
            + toString(_identifier_);
    }

    void removeChild(Node child)
    {
        if(_identifierList_ == child)
        {
            _identifierList_ = null;
            return;
        }

        if(_comma_ == child)
        {
            _comma_ = null;
            return;
        }

        if(_identifier_ == child)
        {
            _identifier_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_identifierList_ == oldChild)
        {
            setIdentifierList((PIdentifierList) newChild);
            return;
        }

        if(_comma_ == oldChild)
        {
            setComma((TComma) newChild);
            return;
        }

        if(_identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
            return;
        }

    }
}