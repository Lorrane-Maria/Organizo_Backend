import React from 'react';
import './Input.css';

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label: string;
}

/**
 * Componente de campo de texto com label.
 */
export const Input: React.FC<InputProps> = ({ label, id, ...rest }) => {
  return (
    <div className="input-group">
      <label htmlFor={id}>{label}</label>
      <input id={id} {...rest} className="input-group__field"/>
    </div>
  );
};
