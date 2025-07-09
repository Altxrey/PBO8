package com.literanusa.view;

import com.literanusa.util.ImageUtils;
import javax.swing.*;
import java.awt.*;

public class NavButton extends JButton {

    private final Color NAV_BAR_COLOR = new Color(39, 55, 77);
    private final Color NAV_BAR_ACTIVE_COLOR = new Color(0, 150, 199);

    public NavButton(String text, String iconPath) {
        super(text);
        setIcon(ImageUtils.loadImageIcon(iconPath, 20, 20));
        setFont(new Font("Segoe UI", Font.BOLD, 16));
        setForeground(new Color(221, 230, 237));
        setBackground(NAV_BAR_COLOR);
        setOpaque(true);
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        setHorizontalAlignment(SwingConstants.LEFT);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
    }

    public void setActive(boolean active) {
        setBackground(active ? NAV_BAR_ACTIVE_COLOR : NAV_BAR_COLOR);
    }
}