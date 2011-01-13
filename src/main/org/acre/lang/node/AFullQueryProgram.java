/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AFullQueryProgram extends PQueryProgram
{
    private PQueryProgram _queryProgram_;
    private TSemicolon _semicolon_;
    private PQuery _query_;

    public AFullQueryProgram()
    {
    }

    public AFullQueryProgram(
        PQueryProgram _queryProgram_,
        TSemicolon _semicolon_,
        PQuery _query_)
    {
        setQueryProgram(_queryProgram_);

        setSemicolon(_semicolon_);

        setQuery(_query_);

    }
    public Object clone()
    {
        return new AFullQueryProgram(
            (PQueryProgram) cloneNode(_queryProgram_),
            (TSemicolon) cloneNode(_semicolon_),
            (PQuery) cloneNode(_query_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFullQueryProgram(this);
    }

    public PQueryProgram getQueryProgram()
    {
        return _queryProgram_;
    }

    public void setQueryProgram(PQueryProgram node)
    {
        if(_queryProgram_ != null)
        {
            _queryProgram_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _queryProgram_ = node;
    }

    public TSemicolon getSemicolon()
    {
        return _semicolon_;
    }

    public void setSemicolon(TSemicolon node)
    {
        if(_semicolon_ != null)
        {
            _semicolon_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _semicolon_ = node;
    }

    public PQuery getQuery()
    {
        return _query_;
    }

    public void setQuery(PQuery node)
    {
        if(_query_ != null)
        {
            _query_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _query_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_queryProgram_)
            + toString(_semicolon_)
            + toString(_query_);
    }

    void removeChild(Node child)
    {
        if(_queryProgram_ == child)
        {
            _queryProgram_ = null;
            return;
        }

        if(_semicolon_ == child)
        {
            _semicolon_ = null;
            return;
        }

        if(_query_ == child)
        {
            _query_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_queryProgram_ == oldChild)
        {
            setQueryProgram((PQueryProgram) newChild);
            return;
        }

        if(_semicolon_ == oldChild)
        {
            setSemicolon((TSemicolon) newChild);
            return;
        }

        if(_query_ == oldChild)
        {
            setQuery((PQuery) newChild);
            return;
        }

    }
}