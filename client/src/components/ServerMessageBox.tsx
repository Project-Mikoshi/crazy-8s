import React from 'react'
import { Container, Stack, Typography } from '@mui/material'

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
    <Container>
      <Typography variant='h4'>Server Messages</Typography>
      <Stack direction='column'>
        {messages.map((message, index) => (
          <Typography variant='body1' key={index}>{message}</Typography>
        ))}
      </Stack>
    </Container>
  )
}
