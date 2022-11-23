import React from 'react'
import PerfectScrollbar from 'react-perfect-scrollbar'
import { Card, CardContent, Divider, Stack, Typography } from '@mui/material'

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
    <Card sx={{ width: '100%' }}>
      <CardContent>
        <Typography variant='h4'>Server Messages</Typography>
        <Divider>logs</Divider>
        <Stack direction='column' spacing={1} sx={{ maxHeight: '275px' }}>
          <PerfectScrollbar>
            {messages.map((message, index) => (
              <Typography variant='body1' key={index}>{message}</Typography>
            ))}
          </PerfectScrollbar>
        </Stack>
      </CardContent>
    </Card>
  )
}
