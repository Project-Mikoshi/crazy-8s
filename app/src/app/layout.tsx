"use client";

import React from 'react'
import { AppBar, Toolbar, Typography } from '@mui/material'

interface LayoutProps {
  children: React.ReactNode
}

export default function RootLayout (props: LayoutProps) {
  // == Props ================================
  const { children } = props

  // == Hooks ================================

  // == Functions ============================

  // == Actions ==============================

  // == Template =============================
  return (
    <html>
      <head />
      <body>
        <div className='app'>
          <AppBar position='static' sx={{ padding: '1rem' }}>
            <Toolbar>
              <Typography variant="h6" sx={{ flexGrow: 1 }}>
                  Crazy 8s
              </Typography>
            </Toolbar>
          </AppBar>

          {children}
        </div>
      </body>
    </html>
  )
}
