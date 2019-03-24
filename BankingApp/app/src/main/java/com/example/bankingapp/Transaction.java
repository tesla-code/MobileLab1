package com.example.bankingapp;

import java.time.LocalDateTime;

/**
 * @author  Victor
 * @version 1
 * @since   24/01/2019
 *
 * The transaction class is used to
 */
public class Transaction
{
    private String m_recipient;
    private int euro_after;
    private int cents_after;
    private int m_transEuro;
    private int m_transCents;
    private LocalDateTime now;

    private int hr;
    private int min;
    private int sec;

    public Transaction(String recipient, int t_euro, int t_cents, int balance, int cents)
    {
        this.m_transEuro = t_euro;
        this.m_transCents = t_cents;
        this.m_recipient = recipient;
        this.euro_after = balance;
        this.cents_after =  cents;

        now = LocalDateTime.now();
        this.hr = now.getHour();
        this.min = now.getMinute();
        this.sec = now.getSecond();

    }

    /**
     *  Constructor used for Angel
     */
    public Transaction(String recipient, int t_euro, int t_cents)
    {
        this.m_transCents = t_cents;
        this.m_transEuro = t_euro;
        this.m_recipient = recipient;
        this.euro_after = t_euro;
        this.cents_after = t_cents;

        now = LocalDateTime.now();
        this.hr = now.getHour();
        this.min = now.getMinute();
        this.sec = now.getSecond();

    }

    @Override
    public String toString()
    {
        String s = hr + ":" + min + ":" + sec+ "  |  " + m_recipient + "  |  " + m_transEuro + "." + m_transCents
                      + "  |  " + euro_after + "." + cents_after;
        return s;
    }

    public void prepareCents()
    {
        m_transCents %= 100;
    }
}
