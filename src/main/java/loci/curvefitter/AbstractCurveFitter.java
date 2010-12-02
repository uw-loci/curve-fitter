//
// AbstractCurveFitter.java
//

/*
Curve Fitter library for fitting exponential decay curves.

Copyright (c) 2010, UW-Madison LOCI
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the UW-Madison LOCI nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/

package loci.curvefitter;

/**
 * Abstract base class for curve fitters.
 *
 * @author Aivar Grislis
 *
 */
public abstract class AbstractCurveFitter implements ICurveFitter {
    FitFunction m_fitFunction;
    double m_xInc = ICurveFitter.DEFAULT_X_INC;
    boolean[] m_free;
    double[] m_instrumentResponse;
    
    /**
     * @inheritDoc
     */
    public FitFunction getFitFunction() {
        return m_fitFunction;
    }
    
    /**
     * @inheritDoc
     */
    public void setFitFunction(FitFunction function) {
        m_fitFunction = function;
    }

    /**
     * @inheritDoc
     */
    public int getNumberComponents() {
        int number = 0;
        if (null != m_fitFunction) {
            int fitFunctionComponents[] = { 1, 2, 3, 1 };
            number = fitFunctionComponents[m_fitFunction.ordinal()];
        }
        return number;
    }

    /**
     * @inheritDoc
     */
    public double getXInc() {
        return m_xInc;
    }

    /**
     * @inheritDoc
     */
    public void setXInc(double xInc) {
        m_xInc = xInc;
    }

    /**
     * @inheritDoc
     */
    public boolean[] getFree() {
        return m_free;
    }

    /**
     * @inheritDoc
     */
    public void setFree(boolean[] free) {
        m_free = free;
    }

    /**
     * @inheritDoc
     */
    public double[] getInstrumentResponse() {
        return m_instrumentResponse;
    }

    /**
     * @inheritDoc
     */
    public void setInstrumentResponse(double response[]) {
        m_instrumentResponse = response;
    }

    /**
     * @inheritDoc
     */
     public int fitData(ICurveFitData[] data) {
        int nData = data[0].getYCount().length;
        return fitData(data, 0, nData - 1);
    }

    /**
     * @inheritDoc
     */
    abstract public int fitData(ICurveFitData[] data, int start, int stop);
}

