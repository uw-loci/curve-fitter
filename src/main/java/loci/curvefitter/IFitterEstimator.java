/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loci.curvefitter;

/**
 * This is an interface used when doing a fit with an RLD estimate fit combined
 * with a LMA fit.  It allows for some peculiarities of TRI2.
 *
 * @author Aivar Grislis
 */
public interface IFitterEstimator {

    /**
     * TRI2 does not use a prompt for the initial RLD estimate fit.
     * 
     * @return 
     */
    public boolean usePrompt();

    /**
     * TRI2 adjusts the start index for the initial RLD estimate.
     * 
     * @param yCount
     * @param start
     * @param stop
     * @return adjusted start index
     */
    public int getEstimateStartIndex(double[] yCount, int start, int stop);

    /**
     * TRI2 adjusts the starting A estimate for the initial RLD estimate fit.
     * 
     * @param A
     * @param yCount
     * @param start
     * @param stop
     * @return initial A estimate
     */
    public double getEstimateAValue(double A, double[] yCount, int start, int stop);
}