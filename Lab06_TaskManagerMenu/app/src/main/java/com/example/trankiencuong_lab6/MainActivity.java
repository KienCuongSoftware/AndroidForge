package com.example.trankiencuong_lab6;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayoutMain;
    private MaterialToolbar materialToolbarMain;
    private NavigationView navigationViewMain;
    private RecyclerView recyclerViewCongViec;
    private FloatingActionButton fabThemCongViec;
    private SearchView searchViewTimCongViec;

    private final List<CongViec> danhSachCongViec = new ArrayList<>();
    private CongViecAdapter adapter;
    private int idCounter = 1;

    private CongViec congViecDangChon;
    private int viTriDangChon = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        khoiTaoView();
        khoiTaoDuLieu();
        cauHinhToolbar();
        cauHinhRecyclerView();
        cauHinhNavigationDrawer();
        cauHinhFAB();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayoutMain.isDrawerOpen(GravityCompat.START)) {
                    drawerLayoutMain.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    private void khoiTaoView() {
        drawerLayoutMain = findViewById(R.id.main);
        materialToolbarMain = findViewById(R.id.materialToolbarMain);
        navigationViewMain = findViewById(R.id.navigationViewMain);
        recyclerViewCongViec = findViewById(R.id.recyclerViewCongViec);
        fabThemCongViec = findViewById(R.id.fabThemCongViec);
    }

    private void khoiTaoDuLieu() {
        danhSachCongViec.add(new CongViec(idCounter++, getString(R.string.sample_task_1), "CONG_VIEC"));
        danhSachCongViec.add(new CongViec(idCounter++, getString(R.string.sample_task_2), "CA_NHAN"));
        danhSachCongViec.add(new CongViec(idCounter++, getString(R.string.sample_task_3), "KHAN_CAP"));
        danhSachCongViec.add(new CongViec(idCounter++, getString(R.string.sample_task_4), "CONG_VIEC"));
    }

    private void cauHinhToolbar() {
        setSupportActionBar(materialToolbarMain);
        materialToolbarMain.setNavigationOnClickListener(v -> drawerLayoutMain.openDrawer(GravityCompat.START));
    }

    private void cauHinhRecyclerView() {
        // Truyền bản sao để tránh adapter sửa trực tiếp danh sách gốc,
        // từ đó lọc nhiều lần vẫn giữ được toàn bộ dữ liệu.
        adapter = new CongViecAdapter(new ArrayList<>(danhSachCongViec));
        recyclerViewCongViec.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCongViec.setAdapter(adapter);

        adapter.setOnItemClickListener(new CongViecAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CongViec congViec, int position) {
                Toast.makeText(
                        MainActivity.this,
                        getString(R.string.toast_da_chon, congViec.getTenCongViec()),
                        Toast.LENGTH_SHORT
                ).show();
            }

            @Override
            public void onItemLongClick(CongViec congViec, int position, View view) {
                congViecDangChon = congViec;
                viTriDangChon = position;
                registerForContextMenu(view);
                view.showContextMenu();
                unregisterForContextMenu(view);
            }

            @Override
            public void onCheckBoxClick(CongViec congViec, int position) {
                congViec.setDaHoanThanh(!congViec.isDaHoanThanh());
                adapter.notifyItemChanged(position);
                Toast.makeText(
                        MainActivity.this,
                        congViec.isDaHoanThanh() ? getString(R.string.toast_da_hoan_thanh) : getString(R.string.toast_chua_hoan_thanh),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void cauHinhNavigationDrawer() {
        navigationViewMain.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_tat_ca) {
                hienThiTatCaCongViec();
            } else if (id == R.id.nav_cong_viec) {
                locCongViecTheoLoai("CONG_VIEC");
            } else if (id == R.id.nav_ca_nhan) {
                locCongViecTheoLoai("CA_NHAN");
            } else if (id == R.id.nav_khan_cap) {
                locCongViecTheoLoai("KHAN_CAP");
            } else if (id == R.id.nav_hoan_thanh) {
                hienThiCongViecHoanThanh();
            } else if (id == R.id.nav_cai_dat) {
                Toast.makeText(this, R.string.toast_cai_dat, Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_gioi_thieu) {
                hienThiGioiThieu();
            }

            drawerLayoutMain.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void cauHinhFAB() {
        fabThemCongViec.setOnClickListener(v -> hienThiPopupMenuFAB(v));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchViewTimCongViec = (SearchView) searchItem.getActionView();
        if (searchViewTimCongViec != null) {
            searchViewTimCongViec.setQueryHint(getString(R.string.search_hint));
            searchViewTimCongViec.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    adapter.filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.filter(newText);
                    return false;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sap_xep_ten) {
            sapXepTheoTen();
            return true;
        } else if (id == R.id.action_sap_xep_ngay) {
            sapXepTheoNgay();
            return true;
        } else if (id == R.id.action_loc_cong_viec) {
            locCongViecTheoLoai("CONG_VIEC");
            return true;
        } else if (id == R.id.action_loc_ca_nhan) {
            locCongViecTheoLoai("CA_NHAN");
            return true;
        } else if (id == R.id.action_loc_khan_cap) {
            locCongViecTheoLoai("KHAN_CAP");
            return true;
        } else if (id == R.id.action_loc_hoan_thanh) {
            hienThiCongViecHoanThanh();
            return true;
        } else if (id == R.id.action_xoa_tat_ca) {
            xoaTatCaCongViec();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (congViecDangChon == null) {
            return super.onContextItemSelected(item);
        }
        int id = item.getItemId();
        if (id == R.id.context_sua) {
            hienThiDialogSuaCongViec();
            return true;
        } else if (id == R.id.context_xoa) {
            xoaCongViecDangChon();
            return true;
        } else if (id == R.id.context_danh_dau) {
            danhDauHoanThanh();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void hienThiPopupMenuFAB(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_fab_popup, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.popup_them_cong_viec) {
                hienThiDialogThemCongViec("CONG_VIEC");
                return true;
            } else if (id == R.id.popup_them_ca_nhan) {
                hienThiDialogThemCongViec("CA_NHAN");
                return true;
            } else if (id == R.id.popup_them_khan_cap) {
                hienThiDialogThemCongViec("KHAN_CAP");
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void hienThiDialogThemCongViec(String loaiCongViec) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_them_cong_viec, null);
        EditText editTextTenCongViec = dialogView.findViewById(R.id.editTextTenCongViec);

        builder.setTitle(getString(R.string.title_them_cong_viec, layTenLoaiCongViec(loaiCongViec)))
                .setView(dialogView)
                .setPositiveButton(R.string.btn_them, (dialog, which) -> {
                    String tenCongViec = editTextTenCongViec.getText().toString().trim();
                    if (!tenCongViec.isEmpty()) {
                        CongViec congViecMoi = new CongViec(idCounter++, tenCongViec, loaiCongViec);
                        danhSachCongViec.add(congViecMoi);
                        adapter.updateDanhSach(danhSachCongViec);
                        Toast.makeText(this, R.string.toast_da_them_cong_viec, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, R.string.empty_error, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.btn_huy, null)
                .show();
    }

    private void hienThiDialogSuaCongViec() {
        if (congViecDangChon == null || viTriDangChon < 0) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_sua_cong_viec, null);
        EditText editTextTenCongViec = dialogView.findViewById(R.id.editTextTenCongViec);
        editTextTenCongViec.setText(congViecDangChon.getTenCongViec());

        builder.setTitle(R.string.title_sua_cong_viec)
                .setView(dialogView)
                .setPositiveButton(R.string.btn_luu, (dialog, which) -> {
                    String tenMoi = editTextTenCongViec.getText().toString().trim();
                    if (!tenMoi.isEmpty()) {
                        congViecDangChon.setTenCongViec(tenMoi);
                        adapter.notifyItemChanged(viTriDangChon);
                        Toast.makeText(this, R.string.toast_da_cap_nhat_cong_viec, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, R.string.empty_error, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.btn_huy, null)
                .show();
    }

    private void xoaCongViecDangChon() {
        if (congViecDangChon == null) {
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_xac_nhan_xoa)
                .setMessage(R.string.dialog_ban_co_chac_muon_xoa_cong_viec_nay)
                .setPositiveButton(R.string.btn_xoa, (dialog, which) -> {
                    danhSachCongViec.remove(congViecDangChon);
                    adapter.updateDanhSach(danhSachCongViec);
                    Toast.makeText(this, R.string.toast_da_xoa_cong_viec, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.btn_huy, null)
                .show();
    }

    private void danhDauHoanThanh() {
        if (congViecDangChon == null || viTriDangChon < 0) {
            return;
        }
        congViecDangChon.setDaHoanThanh(!congViecDangChon.isDaHoanThanh());
        adapter.notifyItemChanged(viTriDangChon);
        Toast.makeText(
                this,
                congViecDangChon.isDaHoanThanh() ? getString(R.string.toast_da_danh_dau_hoan_thanh) : getString(R.string.toast_da_bo_danh_dau),
                Toast.LENGTH_SHORT
        ).show();
    }

    private void xoaTatCaCongViec() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_xac_nhan_xoa_tat_ca)
                .setMessage(R.string.dialog_ban_co_chac_muon_xoa_toan_bo_cong_viec)
                .setPositiveButton(R.string.btn_xoa, (dialog, which) -> {
                    danhSachCongViec.clear();
                    adapter.updateDanhSach(danhSachCongViec);
                    Toast.makeText(this, R.string.toast_da_xoa_tat_ca, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.btn_huy, null)
                .show();
    }

    private void sapXepTheoTen() {
        Collections.sort(danhSachCongViec, (cv1, cv2) -> cv1.getTenCongViec().compareTo(cv2.getTenCongViec()));
        adapter.updateDanhSach(danhSachCongViec);
        Toast.makeText(this, R.string.toast_da_sap_xep_ten, Toast.LENGTH_SHORT).show();
    }

    private void sapXepTheoNgay() {
        Collections.sort(danhSachCongViec, (cv1, cv2) -> Long.compare(cv2.getNgayTao(), cv1.getNgayTao()));
        adapter.updateDanhSach(danhSachCongViec);
        Toast.makeText(this, R.string.toast_da_sap_xep_ngay, Toast.LENGTH_SHORT).show();
    }

    private void hienThiTatCaCongViec() {
        adapter.updateDanhSach(danhSachCongViec);
        Toast.makeText(this, R.string.toast_hien_thi_tat_ca, Toast.LENGTH_SHORT).show();
    }

    private void locCongViecTheoLoai(String loai) {
        List<CongViec> danhSachLoc = new ArrayList<>();
        for (CongViec cv : danhSachCongViec) {
            if (cv.getLoaiCongViec().equals(loai)) {
                danhSachLoc.add(cv);
            }
        }
        adapter.updateDanhSach(danhSachLoc);
        Toast.makeText(this, getString(R.string.toast_loc_theo_loai, layTenLoaiCongViec(loai)), Toast.LENGTH_SHORT).show();
    }

    private void hienThiCongViecHoanThanh() {
        List<CongViec> danhSachLoc = new ArrayList<>();
        for (CongViec cv : danhSachCongViec) {
            if (cv.isDaHoanThanh()) {
                danhSachLoc.add(cv);
            }
        }
        adapter.updateDanhSach(danhSachLoc);
        Toast.makeText(this, R.string.toast_loc_da_hoan_thanh, Toast.LENGTH_SHORT).show();
    }

    private void hienThiGioiThieu() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_gioi_thieu)
                .setMessage(R.string.dialog_gioi_thieu_noi_dung)
                .setPositiveButton(R.string.btn_dong, null)
                .show();
    }

    private String layTenLoaiCongViec(String maLoai) {
        if ("CONG_VIEC".equals(maLoai)) {
            return getString(R.string.loai_cong_viec);
        }
        if ("CA_NHAN".equals(maLoai)) {
            return getString(R.string.loai_ca_nhan);
        }
        if ("KHAN_CAP".equals(maLoai)) {
            return getString(R.string.loai_khan_cap);
        }
        return maLoai;
    }
}