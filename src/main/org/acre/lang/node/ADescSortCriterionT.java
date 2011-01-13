/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ADescSortCriterionT extends PSortCriterionT
{
    private TDesc _desc_;

    public ADescSortCriterionT()
    {
    }

    public ADescSortCriterionT(
        TDesc _desc_)
    {
        setDesc(_desc_);

    }
    public Object clone()
    {
        return new ADescSortCriterionT(
            (TDesc) cloneNode(_desc_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseADescSortCriterionT(this);
    }

    public TDesc getDesc()
    {
        return _desc_;
    }

    public void setDesc(TDesc node)
    {
        if(_desc_ != null)
        {
            _desc_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _desc_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_desc_);
    }

    void removeChild(Node child)
    {
        if(_desc_ == child)
        {
            _desc_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_desc_ == oldChild)
        {
            setDesc((TDesc) newChild);
            return;
        }

    }
}