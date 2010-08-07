/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package loci.curvefitter;

import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

import ij.*;

/**
 *
 * @author aivar
 */
public class GrayCurveFitter extends AbstractCurveFitter {
    int m_algType;

    public interface CLibrary extends Library {

        public int nr_GCI_triple_integral_fitting_engine(float xincr, float y[], int fitStart, int fitEnd,
									   float instr[], int nInstr, int noise, float sig[],
                                                                           FloatByReference z, FloatByReference a, FloatByReference tau,
                                                                           float fitted[], float residuals[],
                                                                           FloatByReference chiSq, float chiSqTarget);

        public int nr_GCI_marquardt_fitting_engine(float xincr, float y[], int nData, int fitStart, int fitEnd,
                                                                           float instr[], int nInstr, int noise, float sig[],
                                                                           float param[], int paramFree[], int nParam,
                                                                           int restrainType, int fitType,
                                                                           float fitted[], float residuals[],
                                                                           FloatByReference chiSq);
        //,
        //                                                                   float covar[], float alpha[], float errAxes[],
        //                                                                   float chiSqTarget, int chiSqPercent);

        
        /*public int nr_GCI_marquardt_fitting_engine(float xincr, float *trans, int ndata, int fit_start, int fit_end,
								 float prompt[], int nprompt, //TODO ARG is this actually instr[] & ninstr?
								 noise_type noise, float sig[],
								 float param[], int paramfree[],
								 int nparam, restrain_type restrain,
								 fit_type fit, //TODO ARG void (*fitfunc)(float, float [], float *, float [], int),
								 float *fitted, float *residuals, float *chisq,
								 float **covar, float **alpha, float **erraxes,
									float chisq_target, int chisq_percent) {*/
    }

    public GrayCurveFitter(int algType) {
        m_algType = algType;
    }

    public GrayCurveFitter() {
        m_algType = 0;
    }


    /**
     * @inheritDoc
     */
    public int fitData(ICurveFitData[] dataArray, int start, int stop) {

 	CLibrary lib = (CLibrary) Native.loadLibrary("GrayCode", CLibrary.class);

        //TODO ARG since initial x = fit_start * xincr we have to supply the unused portion of y[] before fit_start.
        // if this data were already premassaged it might be better to get rid of fit_start & _end, just give the
        // portion to be fitted and specify an initial x.
        //TODO ARG August use initial X of 0.

        if (0 == m_algType) {
            float xincr = (float) m_xInc;
            int fitStart = start;
            int fitEnd = stop;
            float instr[] = new float[0];
            int nInstr = 0; // no lamp function
            int noise = 2; // Poisson noise
            float sig[] = new float[stop+1];
            for (int i = 0; i < sig.length; ++i) {
                sig[i] = 1.0f; // ignore sig
            }
            FloatByReference z = new FloatByReference(1.0f); // starting guesses
            FloatByReference a = new FloatByReference(1.0f);
            FloatByReference tau = new FloatByReference(1.0f);
            float fitted[] = new float[stop+1];
            float residuals[] = new float[stop+1];
            FloatByReference chiSq = new FloatByReference();
            float chiSqTarget = 1.0f; //TODO note 'chiSqTarget' is internally known as 'division'!!!!

            boolean success;
            int goodPixels = 0;
            int badPixels = 0;

           // double[][] lmaData;

           // int length = stop - start + 1;
           // lmaData = new double[2][length];
            //double x_value = start * m_xInc;
            //for (int i = 0; i < length; ++i) {
            //    lmaData[0][i] = x_value;
            //    x_value += m_xInc;
            //}

            double params[] = new double[3];
            for (ICurveFitData data: dataArray) {
                float y[] = doubleToFloat(data.getYData());

                //for (int i = 0; i < y.length; ++i) {
                //    System.out.println("y[" + i + "] is " + y[i]);
                //}

                params = data.getParams();
                a.setValue((float) params[0]);
                tau.setValue(1.0f / ((float) params[1])); // convert lambda to tau
                z.setValue((float) params[2]);



System.out.println("xinc " + xincr + " start " + fitStart + " end " + fitEnd + " chiSqTarget " + chiSqTarget);
                int returnValue = lib.nr_GCI_triple_integral_fitting_engine(xincr, y, fitStart, fitEnd,
									   instr, nInstr, noise, sig,
                                                                           z, a, tau,
                                                                           fitted, residuals,
                                                                           chiSq, chiSqTarget);
System.out.println("returnValue " + returnValue + " z " + z.getValue() + " a " + a.getValue() + " tau " + tau.getValue() + " chiSq " + chiSq.getValue());
                //data.setYFitted();
                params[0] = (double) a.getValue();
                params[1] = 1.0 / ((double) tau.getValue()); // convert tau to lambda
                params[2] = (double) z.getValue();
                data.setParams(params);
            }
        }
        else {
            float xincr = (float) m_xInc;
            int fitStart = start;
            int fitEnd = stop;
            float instr[] = new float[0];
            int nInstr = 0; // no lamp function
            int noise = 2; // Poisson noise
            float sig[] = new float[stop+1];
            for (int i = 0; i < sig.length; ++i) {
                sig[i] = 1.0f; // ignore sig
            }
           // FloatByReference z = new FloatByReference(1.0f); // starting guesses
           // FloatByReference a = new FloatByReference(1.0f);
           // FloatByReference tau = new FloatByReference(1.0f);
            float fitted[] = new float[stop+1];
            float residuals[] = new float[stop+1];
            FloatByReference chiSq = new FloatByReference();
            float chiSqTarget = 1.0f; //TODO note 'chiSqTarget' is internally known as 'division'!!!!

            boolean success;
            int goodPixels = 0;
            int badPixels = 0;

           // double[][] lmaData;
//
           // int length = stop - start + 1;
           // lmaData = new double[2][length];
           // double x_value = start * m_xInc;
           // for (int i = 0; i < length; ++i) {
           //     lmaData[0][i] = x_value;
           //     x_value += m_xInc;
           // }

            // new for LMA
            double params[] = new double[3];

            for (ICurveFitData data: dataArray) {
                int nData = data.getYData().length;
                float y[] = doubleToFloat(data.getYData());

                //for (int i = 0; i < y.length; ++i) {
                //    System.out.println("y[" + i + "] is " + y[i]);
                //}

                float param[] = new float[3];
                param[0] = (float) data.getParams()[2];        // z
                param[1] = (float) data.getParams()[0];        // A
                param[2] = 1.0f / (float) data.getParams()[1]; // tau from lambda

                System.out.println("Incoming params z " + data.getParams()[2] + " A " + data.getParams()[0] + " lambda " + data.getParams()[1]);
 
// float param[] = doubleToFloat(data.getParams());
                int nParams = param.length;
      System.out.println("nParams is " + nParams);
// param[1] = 1.0f / param[1];
                int paramFree[] = new int[nParams];
                for (int i = 0; i < nParams; ++i) {
                    paramFree[i] = 1;
                }
                System.out.println("paramFree length is " + paramFree.length);
                int restrainType = 0; //TODO 1 doesn't work; internally s/b calling "GCS_set_restrain_limits" but doesn't!!
                int fitType = 0;
                //float covar[][] = new float[stop+1][stop+1];
                //float alpha[][] = new float[stop+1][stop+1];
                //float errAxes[][] = new float[stop+1][stop+1];
                int chiSqPercent = 500;

                float covarX[] = new float[(stop+1)*(stop+1)];
                float alphaX[] = new float[(stop+1)*(stop+1)];
                float errAxesX[] = new float[(stop+1)*(stop+1)];

System.out.println("xinc " + xincr + " start " + fitStart + " end " + fitEnd + " chiSqTarget " + chiSqTarget);
                int returnValue = lib.nr_GCI_marquardt_fitting_engine(xincr, y, nData, fitStart, fitEnd,
                                                                           instr, nInstr, noise, sig,
                                                                           param, paramFree, nParams,
                                                                           restrainType, fitType,
                                                                           fitted, residuals,
                                                                           chiSq);
                //,
                  //                                                         covarX, alphaX, errAxesX,
                    //                                                       chiSqTarget, chiSqPercent);






System.out.println("HELLO returnValue z A tau = " + returnValue + " " + param[0] + " " + param[1] + " " + param[2]);
                //data.setYFitted()
                double dParam[] = new double[3];
                dParam[0] = param[1];       // A
                dParam[1] = 1.0 / param[2]; // lambda from tau
                dParam[2] = param[0];       // z
                data.setParams(dParam);
System.out.println("returning A " + dParam[0] + " lambda " + dParam[1] + " z " + dParam[2]);


//param[1] = 1.0f / param[1];
//data.setParams(floatToDouble(param));
            }

        }

        //TODO error return deserves more thought
        return 0;
    }

    double[] floatToDouble(float[] f) {
        double d[] = new double[f.length];
        for (int i = 0; i < f.length; ++i) {
            d[i] = f[i];
        }
        return d;
    }

    float[] doubleToFloat(double[] d) {
        float f[] = new float[d.length];
        for (int i = 0; i < d.length; ++i) {
            f[i] = (float) d[i];
        }
        return f;
    }

}