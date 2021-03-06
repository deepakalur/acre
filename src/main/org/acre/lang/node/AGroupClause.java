/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AGroupClause extends PGroupClause
{
    private TGroup _group_;
    private TBy _by_;
    private PFieldList _fieldList_;
    private PHavingClause _havingClause_;

    public AGroupClause()
    {
    }

    public AGroupClause(
        TGroup _group_,
        TBy _by_,
        PFieldList _fieldList_,
        PHavingClause _havingClause_)
    {
        setGroup(_group_);

        setBy(_by_);

        setFieldList(_fieldList_);

        setHavingClause(_havingClause_);

    }
    public Object clone()
    {
        return new AGroupClause(
            (TGroup) cloneNode(_group_),
            (TBy) cloneNode(_by_),
            (PFieldList) cloneNode(_fieldList_),
            (PHavingClause) cloneNode(_havingClause_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAGroupClause(this);
    }

    public TGroup getGroup()
    {
        return _group_;
    }

    public void setGroup(TGroup node)
    {
        if(_group_ != null)
        {
            _group_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _group_ = node;
    }

    public TBy getBy()
    {
        return _by_;
    }

    public void setBy(TBy node)
    {
        if(_by_ != null)
        {
            _by_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _by_ = node;
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

    public PHavingClause getHavingClause()
    {
        return _havingClause_;
    }

    public void setHavingClause(PHavingClause node)
    {
        if(_havingClause_ != null)
        {
            _havingClause_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _havingClause_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_group_)
            + toString(_by_)
            + toString(_fieldList_)
            + toString(_havingClause_);
    }

    void removeChild(Node child)
    {
        if(_group_ == child)
        {
            _group_ = null;
            return;
        }

        if(_by_ == child)
        {
            _by_ = null;
            return;
        }

        if(_fieldList_ == child)
        {
            _fieldList_ = null;
            return;
        }

        if(_havingClause_ == child)
        {
            _havingClause_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_group_ == oldChild)
        {
            setGroup((TGroup) newChild);
            return;
        }

        if(_by_ == oldChild)
        {
            setBy((TBy) newChild);
            return;
        }

        if(_fieldList_ == oldChild)
        {
            setFieldList((PFieldList) newChild);
            return;
        }

        if(_havingClause_ == oldChild)
        {
            setHavingClause((PHavingClause) newChild);
            return;
        }

    }
}
