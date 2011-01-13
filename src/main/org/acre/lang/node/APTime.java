/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class APTime extends PPTime
{
    private TTime _time_;
    private TQuote _q1_;
    private PTimeContent _timeContent_;
    private TQuote _q2_;

    public APTime()
    {
    }

    public APTime(
        TTime _time_,
        TQuote _q1_,
        PTimeContent _timeContent_,
        TQuote _q2_)
    {
        setTime(_time_);

        setQ1(_q1_);

        setTimeContent(_timeContent_);

        setQ2(_q2_);

    }
    public Object clone()
    {
        return new APTime(
            (TTime) cloneNode(_time_),
            (TQuote) cloneNode(_q1_),
            (PTimeContent) cloneNode(_timeContent_),
            (TQuote) cloneNode(_q2_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAPTime(this);
    }

    public TTime getTime()
    {
        return _time_;
    }

    public void setTime(TTime node)
    {
        if(_time_ != null)
        {
            _time_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _time_ = node;
    }

    public TQuote getQ1()
    {
        return _q1_;
    }

    public void setQ1(TQuote node)
    {
        if(_q1_ != null)
        {
            _q1_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _q1_ = node;
    }

    public PTimeContent getTimeContent()
    {
        return _timeContent_;
    }

    public void setTimeContent(PTimeContent node)
    {
        if(_timeContent_ != null)
        {
            _timeContent_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _timeContent_ = node;
    }

    public TQuote getQ2()
    {
        return _q2_;
    }

    public void setQ2(TQuote node)
    {
        if(_q2_ != null)
        {
            _q2_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _q2_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_time_)
            + toString(_q1_)
            + toString(_timeContent_)
            + toString(_q2_);
    }

    void removeChild(Node child)
    {
        if(_time_ == child)
        {
            _time_ = null;
            return;
        }

        if(_q1_ == child)
        {
            _q1_ = null;
            return;
        }

        if(_timeContent_ == child)
        {
            _timeContent_ = null;
            return;
        }

        if(_q2_ == child)
        {
            _q2_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_time_ == oldChild)
        {
            setTime((TTime) newChild);
            return;
        }

        if(_q1_ == oldChild)
        {
            setQ1((TQuote) newChild);
            return;
        }

        if(_timeContent_ == oldChild)
        {
            setTimeContent((PTimeContent) newChild);
            return;
        }

        if(_q2_ == oldChild)
        {
            setQ2((TQuote) newChild);
            return;
        }

    }
}
