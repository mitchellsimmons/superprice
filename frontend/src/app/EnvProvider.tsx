'use client'
import { createContext, useContext, ReactNode } from 'react'

export const EnvContext = createContext<Record<string, string | undefined>>({})

interface EvnProviderProps {
  children: ReactNode
  env: Record<string, string | undefined>
}

export const EnvProvider = ({ children, env }: EvnProviderProps) => {
  return <EnvContext.Provider value={env}>{children}</EnvContext.Provider>
}

export const useEnvContext = () => useContext(EnvContext)