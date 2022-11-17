import React from 'react'
import { Container, Stack, Typography } from '@mui/material'

interface MessageBoxProps {
  messages: Array<String>
}

export default function MessageBox (props: MessageBoxProps) {
  // == Props ================================
  const { messages } = props

  // == States ===============================

  // == Lifecycle ============================

  // == Functions ============================

  // == Actions ==============================

  // == Template =============================

  return (
    <Container>
      <Stack direction='column'>
        {messages.map((message, index) => (
          <Typography key={index}>{message}</Typography>
        ))}
      </Stack>
    </Container>
  )
}
