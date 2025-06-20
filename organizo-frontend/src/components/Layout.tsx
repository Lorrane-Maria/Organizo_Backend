import React from 'react';
import Navbar from './Navbar';
import './Layout.css';

interface LayoutProps {
  children: React.ReactNode;
}

/**
 * Layout principal da aplicação:
 * - Navbar fixa no topo
 * - Container centralizado
 */
const Layout: React.FC<LayoutProps> = ({ children }) => (
  <div className="layout">
    <Navbar />
    <main className="layout__content">{children}</main>
  </div>
);

export default Layout;
