import Navbar from './components/navbar.tsx';
import { Outlet } from 'react-router-dom';
import BottomBar from './components/bottom-bar.tsx';
import { useEffect } from 'react';

const Layout = () => {
  useEffect(() => console.log(window.navigator.standalone), []);

  return (
    <div
      id="layout"
      className="flex h-[calc(100dvh)] w-screen flex-col">
      <Navbar />
      <Outlet />
      <BottomBar />
    </div>
  );
};

export default Layout;
