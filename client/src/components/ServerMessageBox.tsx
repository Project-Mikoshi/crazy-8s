import React from 'react'
import PerfectScrollbar from 'react-perfect-scrollbar'
import { Stack, Typography } from '@mui/material'

interface MessageBoxProps {
  messages: Array<String>
}

export default function (props: MessageBoxProps) {
  // == Props ================================
  const { messages } = props

  // == States ===============================

  // == Lifecycle ============================

  // == Functions ============================

  // == Actions ==============================

  // == Template =============================
  return (
    <PerfectScrollbar>
      <Typography variant='h4'>Server Messages</Typography>
      <Stack direction='column'>
        {messages.map((message, index) => (
          <Typography variant='body1' key={index}>{message}</Typography>
        ))}
      </Stack>
    </PerfectScrollbar>
  )
}
