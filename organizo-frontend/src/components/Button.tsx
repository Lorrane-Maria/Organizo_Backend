import React from 'react';

export type ButtonVariant = 'primary' | 'secondary';

export interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: ButtonVariant;
}

/**
 * Botão reutilizável com duas variantes de estilo.
 */
export const Button: React.FC<ButtonProps> = ({
  variant = 'primary',
  children,
  className = '',
  ...props
}) => (
  <button
    className={`btn btn--${variant} ${className}`.trim()}
    {...props}
  >
    {children}
  </button>
);
