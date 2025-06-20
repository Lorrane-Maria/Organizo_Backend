import React from 'react';

export interface CardProps {
  children: React.ReactNode;
  className?: string;
}

/**
 * Card genérico para agrupar conteúdo com sombra e padding.
 */
export const Card: React.FC<CardProps> = ({ children, className = '' }) => (
  <div className={`card ${className}`.trim()}>
    {children}
  </div>
);
