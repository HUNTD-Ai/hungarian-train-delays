import Navbar from './components/navbar.tsx';
import { Outlet } from 'react-router-dom';

const Layout = () => {
  return (
    <div
      id="layout"
      className="flex h-screen w-screen flex-col">
      <Navbar />
      <Outlet />
    </div>
  );
};

export default Layout;
